package storm.skyline.bolt;

import com.skyline.EventSkyline;

import storm.skyline.model.StormSkyTuple;

public interface Skyline {

    public EventSkyline<StormSkyTuple> getLayer(Integer layer);

}
