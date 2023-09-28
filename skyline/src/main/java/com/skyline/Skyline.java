package com.skyline;

import java.io.Serializable;
import java.util.List;

import com.skyline.model.SkyStructure;
import com.skyline.model.SkyTuple;

public interface Skyline<T extends SkyTuple> extends TupleListener<T>, Serializable {

    public boolean in(T tuple);

    public SkyStructure<T> getStructure();

    public int getDimensions();

    public int getLayer();

    public void setLayer(int layer);

    public List<T> getTuples();

    public int getCount();

    public void clear();

}
