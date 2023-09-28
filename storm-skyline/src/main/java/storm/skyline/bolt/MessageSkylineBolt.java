package storm.skyline.bolt;

import java.util.Map;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import storm.skyline.config.QueueConfig;
import storm.skyline.messaging.Messenger;
import storm.skyline.messaging.MessengerFactory;
import storm.skyline.model.StormSkyTuple;

public class MessageSkylineBolt extends BaseRichBolt {

    private final static Logger logger = Logger.getLogger(MessageSkylineBolt.class);

    private static final long serialVersionUID = 4246742589195286462L;

    private OutputCollector collector;

    private final Messenger messenger;

    public MessageSkylineBolt(QueueConfig config) {
        this.messenger = MessengerFactory.configure(new Messenger(config));

    }

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        logger.info("MESSAGE BOLT ID: " + context.getThisTaskId());

        try {
            this.messenger.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Tuple input) {
        StormSkyTuple tuple = (StormSkyTuple) input.getValue(0);
        // logger.info("\nMESSAGE: " + tuple + "\n");

        try {
            messenger.send(tuple);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
