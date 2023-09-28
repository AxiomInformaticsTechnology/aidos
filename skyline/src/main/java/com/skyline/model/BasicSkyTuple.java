package com.skyline.model;

public class BasicSkyTuple extends AbstractSkyTuple {

    private static final long serialVersionUID = -3098072525300871428L;

    public BasicSkyTuple() {
        super();
    }

    public BasicSkyTuple(Object[] values) {
        super(values);
    }

    public BasicSkyTuple(Object[] values, Long expiration) {
        super(values, expiration);
    }

    public BasicSkyTuple(Object[] values, String identifier) {
        super(values, identifier);
    }

    public BasicSkyTuple(Object[] values, String identifier, Long expiration) {
        super(values, identifier, expiration);
    }

}
