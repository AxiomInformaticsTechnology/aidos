package storm.skyline.spout;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import com.skyline.model.SkyPart;
import com.skyline.model.SkyStructure;
import com.skyline.utility.RandomUtility;

import storm.skyline.config.RandomSpoutConfig;
import storm.skyline.model.StormSkyTuple;

public class RandomSkylineSpout extends BaseRichSpout {

    private final static Logger logger = Logger.getLogger(RandomSkylineSpout.class);

    private final static long serialVersionUID = 7938918486167394886L;

    private final RandomSpoutConfig config;

    private final SkyStructure<StormSkyTuple> structure;

    private SpoutOutputCollector collector;

    private int count = 0;

    public RandomSkylineSpout(RandomSpoutConfig config, SkyStructure<StormSkyTuple> structure) throws IOException {
        this.config = config;
        this.structure = structure;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {

        int structureScale = structure.getScale();

        int dimensions = structure.getDimensions();

        Object[] values = new Object[dimensions];

        double[] randomValues = RandomUtility.generateRandom(dimensions, config.getType());

        SkyPart[] parts = structure.getParts();
        for (int i = 0; i < dimensions; i++) {
            SkyPart part = parts[i];
            Object value;
            switch (part.getBias()) {
            case VALUE:
                Object[] options = part.getOptions();
                value = options[(int) Math.floor(randomValues[i] * options.length)];
                break;
            case MAX:
            case MIN:
            default:
                value = part.getBase() + (randomValues[i] * structureScale * part.getScale());
                break;
            }
            values[i] = value;
        }

        int random = RandomUtility.randomInteger(config.getTupleDurationMin(), config.getTupleDurationMax());
        long expiration = (random * 1000) + Instant.now().toEpochMilli();

        collector.emit(new Values(new StormSkyTuple(values, expiration)));

        if (++count % 1000000 == 0) {
            logger.info(count + " tuples in topology!");
        }
    }

    @Override
    public void ack(Object id) {

    }

    @Override
    public void fail(Object id) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tuple"));
    }

}
