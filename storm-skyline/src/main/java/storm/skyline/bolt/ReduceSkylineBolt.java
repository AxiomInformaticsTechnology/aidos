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

public class ReduceSkylineBolt extends AbstractSkylineBolt {

    private final static Logger logger = Logger.getLogger(ReduceSkylineBolt.class);

    private static final long serialVersionUID = -1878751278027299715L;

    public ReduceSkylineBolt(QueueConfig config, SkyStructure<StormSkyTuple> structure) {
        super(config, structure);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.id = context.getThisTaskId();
        this.collector = collector;
        logger.info("REDUCE BOLT ID: " + id);

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
                        logger.info("REDUCE BOLT + " + id + " RECEIVED MESSAGE: " + ((ObjectMessage) message).getObject());
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
        // logger.debug("\nREDUCE: " + tuple + "\n");
        EventSkyline<StormSkyTuple> layer = getLayer(tuple.getLayer());
        if (layer.in(tuple)) {
            tuple.setReduceId(id);
            collector.emit("skylineStream", new Values(tuple));
        }
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("skylineStream", new Fields("tuple"));
        declarer.declareStream("inputSkylineStream", new Fields("tuple"));
    }

    @Override
    protected TupleListener<StormSkyTuple> createListener(Integer layer) {
        return new TupleListener<StormSkyTuple>() {

            @Override
            public void up(StormSkyTuple tuple) {
                tuple.layerUp();
                // logger.debug("\nREDUCE UP: " + tuple + "\n");
                collector.emit("inputSkylineStream", new Values(tuple));
            }

            @Override
            public void down(StormSkyTuple tuple) {
                tuple.layerDown();
                // logger.debug("\nREDUCE DOWN: " + tuple + "\n");
                collector.emit("inputSkylineStream", new Values(tuple));
            }

            @Override
            public void expire(StormSkyTuple tuple) {
                // logger.debug("\nREDUCE EXPIRE: " + tuple + "\n");
                collector.emit("storeSkylineStream", new Values(tuple));
            }

            @Override
            public void change() {

            };

        };
    }

}
