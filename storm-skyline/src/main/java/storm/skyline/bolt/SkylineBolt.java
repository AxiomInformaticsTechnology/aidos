package storm.skyline.bolt;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.skyline.EventSkyline;
import com.skyline.TupleListener;
import com.skyline.model.SkyStructure;

import storm.skyline.config.QueueConfig;
import storm.skyline.model.StormSkyTuple;

public class SkylineBolt extends AbstractSkylineBolt {

    private final static Logger logger = Logger.getLogger(SkylineBolt.class);

    private static final long serialVersionUID = -4407699433625703458L;

    public SkylineBolt(QueueConfig config, SkyStructure<StormSkyTuple> structure) {
        super(config, structure);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.id = context.getThisTaskId();
        this.collector = collector;
        logger.info("SKYLINE BOLT ID: " + id);

        try {
            this.messenger.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.messenger.listen(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    try {
                        logger.info("\nSKYLINE RECEIVED MESSAGE: " + ((ObjectMessage) message).getObject() + "\n");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple input) {
        StormSkyTuple tuple = (StormSkyTuple) input.getValue(0);
        // logger.debug("\nSKYLINE: " + tuple + "\n");
        EventSkyline<StormSkyTuple> layer = getLayer(tuple.getLayer());
        if (layer.in(tuple)) {
            Values values = new Values(tuple);
            collector.emit("storeSkylineStream", values);
            collector.emit("messageSkylineStream", values);
        }
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("inputSkylineStream", new Fields("tuple"));
        declarer.declareStream("storeSkylineStream", new Fields("tuple"));
        declarer.declareStream("messageSkylineStream", new Fields("tuple"));
    }

    @Override
    protected TupleListener<StormSkyTuple> createListener(Integer layer) {
        return new TupleListener<StormSkyTuple>() {

            @Override
            public void up(StormSkyTuple tuple) {
                tuple.layerUp();
                // logger.debug("\nSKYLINE UP: " + tuple + "\n");
                collector.emit("inputSkylineStream", new Values(tuple));
            }

            @Override
            public void down(StormSkyTuple tuple) {
                tuple.layerDown();
                // logger.debug("\nSKYLINE DOWN: " + tuple + "\n");
                collector.emit("inputSkylineStream", new Values(tuple));
            }

            @Override
            public void expire(StormSkyTuple tuple) {
                // logger.debug("\nSKYLINE EXPIRE: " + tuple + "\n");
                Values values = new Values(tuple);
                collector.emit("storeSkylineStream", values);
                collector.emit("messageSkylineStream", values);
            }

            @Override
            public void change() {

            };

        };
    }

}
