package storm.skyline.bolt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import storm.skyline.model.StormSkyTuple;
import storm.skyline.model.repo.StormSkyTupleRepo;
import storm.skyline.persistence.SkylineStore;

public class StoreSkylineBolt extends BaseRichBolt {

    private final static Logger logger = Logger.getLogger(StoreSkylineBolt.class);

    private static final long serialVersionUID = 7641005133215453394L;

    private OutputCollector collector;

    private transient StormSkyTupleRepo stormSkyTupleRepo;

    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.stormSkyTupleRepo = SkylineStore.getStormSkyTupleRepo();
        logger.info("STORE BOLT ID: " + context.getThisTaskId());
    }

    @Override
    public void execute(Tuple input) {
        StormSkyTuple tuple = (StormSkyTuple) input.getValue(0);

        if (tuple.isExpired()) {
            // logger.debug("\nSTORE DELETE: " + tuple + "\n");
            stormSkyTupleRepo.delete(tuple);
        } else {
            // logger.debug("\nSTORE SAVE: " + tuple + "\n");
            stormSkyTupleRepo.save(tuple);
        }

        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}
