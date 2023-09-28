package storm.skyline.spout;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import com.skyline.model.BasicSkyTuple;
import com.skyline.utility.TupleLoader;

import storm.skyline.model.StormSkyTuple;

public class FileSkylineSpout extends BaseRichSpout {

    private final static long serialVersionUID = 7938918486167394886L;

    private SpoutOutputCollector collector;

    private List<BasicSkyTuple> tuples;

    private int index = 0;

    public FileSkylineSpout(String path) throws IOException {
        this.tuples = TupleLoader.load(path);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        if (index < tuples.size()) {
            collector.emit(new Values(new StormSkyTuple(tuples.get(index++))));
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
