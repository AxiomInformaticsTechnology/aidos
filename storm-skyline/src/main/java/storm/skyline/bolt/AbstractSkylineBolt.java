package storm.skyline.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.topology.base.BaseRichBolt;

import com.skyline.EventSkyline;
import com.skyline.TupleListener;
import com.skyline.model.SkyStructure;

import storm.skyline.config.QueueConfig;
import storm.skyline.messaging.Messenger;
import storm.skyline.messaging.MessengerFactory;
import storm.skyline.model.StormSkyTuple;

public abstract class AbstractSkylineBolt extends BaseRichBolt implements Skyline {

    private static final long serialVersionUID = 5213574207502268529L;

    protected final Messenger messenger;

    protected final SkyStructure<StormSkyTuple> structure;

    protected final Map<Integer, EventSkyline<StormSkyTuple>> layers;

    protected int id;

    protected OutputCollector collector;

    public AbstractSkylineBolt(QueueConfig config, SkyStructure<StormSkyTuple> structure) {
        super();
        this.messenger = MessengerFactory.configure(new Messenger(config));
        this.structure = structure;
        this.layers = new HashMap<Integer, EventSkyline<StormSkyTuple>>();
    }

    public EventSkyline<StormSkyTuple> getLayer(Integer layer) {
        EventSkyline<StormSkyTuple> skyline = layers.get(layer);
        if (skyline == null) {
            skyline = new EventSkyline<StormSkyTuple>(structure, createListener(layer));
            layers.put(layer, skyline);
        }
        return skyline;
    }

    protected abstract TupleListener<StormSkyTuple> createListener(Integer layer);

}
