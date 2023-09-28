package storm.skyline.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import storm.skyline.model.StormSkyTuple;

public class InputSkylineBolt extends BaseBasicBolt {

    private static final long serialVersionUID = 1181211689923597267L;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        collector.emit("mapSkylineStream", new Values((StormSkyTuple) tuple.getValue(0)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("mapSkylineStream", new Fields("tuple"));
    }

}
