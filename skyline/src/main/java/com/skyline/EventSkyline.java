package com.skyline;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skyline.model.SkyStructure;
import com.skyline.model.SkyTuple;

public class EventSkyline<T extends SkyTuple> extends AbstractSkyline<T> {

    private static final long serialVersionUID = -5451347197356760913L;

    @JsonIgnore
    private TupleListener<T> listener;

    public EventSkyline(SkyStructure<T> structure) {
        super(structure);
    }

    public EventSkyline(SkyStructure<T> structure, TupleListener<T> listener) {
        super(structure);
        this.listener = listener;
    }

    public TupleListener<T> getListener() {
        return listener;
    }

    public void setListener(TupleListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void up(T tuple) {
        listener.up(tuple);
    }

    @Override
    public void down(T tuple) {
        listener.down(tuple);
    }

    @Override
    public void expire(T tuple) {
        listener.expire(tuple);
    }

    @Override
    public void change() {
        listener.change();
    }

}
