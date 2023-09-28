package com.skyline;

import com.skyline.model.SkyTuple;

public interface TupleListener<T extends SkyTuple> {

    public void up(T tuple);

    public void down(T tuple);

    public void expire(T tuple);

    public void change();

}
