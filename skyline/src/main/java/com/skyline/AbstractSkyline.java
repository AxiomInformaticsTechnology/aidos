package com.skyline;

import java.util.ArrayList;
import java.util.List;

import com.skyline.model.SkyAction;
import com.skyline.model.SkyStructure;
import com.skyline.model.SkyTuple;
import com.skyline.utility.MapperUtility;

public abstract class AbstractSkyline<T extends SkyTuple> implements Skyline<T> {

    private static final long serialVersionUID = -2389304676122514200L;

    private final SkyStructure<T> structure;

    private final List<T> tuples;

    private Integer layer;

    public AbstractSkyline(SkyStructure<T> structure) {
        this.structure = structure;
        tuples = new ArrayList<T>();
        layer = 0;
    }

    public AbstractSkyline(SkyStructure<T> structure, Integer layer) {
        this(structure);
        this.layer = layer;
    }

    public synchronized boolean in(T tuple) {
        boolean expired = tuple.isExpired();
        if (expired) {
            expire(tuple);
        }
        boolean inSkyline = !expired;
        boolean removing = tuple.getAction().equals(SkyAction.REMOVAL);
        boolean evaluation = tuple.getAction().equals(SkyAction.EVALUATION);

        if (removing || evaluation) {
            inSkyline = false;
        }

        List<T> tuplesToPromote = new ArrayList<T>();
        List<T> tuplesToDemote = new ArrayList<T>();
        for (T skylineTuple : tuples) {
            if (skylineTuple.isExpired()) {
                skylineTuple.setAction(SkyAction.EVALUATION);
                tuplesToDemote.add(skylineTuple);
                continue;
            }
            if (evaluation && skylineTuple.getDominate() != null && skylineTuple.getDominate().equals(tuple.getIdentifier())) {
                tuplesToPromote.add(skylineTuple);
            }
            if (inSkyline) {
                if (structure.dominates(skylineTuple, tuple)) {
                    tuple.setDominate(skylineTuple.getIdentifier());
                    inSkyline = false;
                } else {
                    if (structure.dominates(tuple, skylineTuple)) {
                        skylineTuple.setDominate(tuple.getIdentifier());
                        tuplesToDemote.add(skylineTuple);
                    }
                }
            }
        }

        tuplesToDemote.forEach(dt -> {
            tuples.remove(dt);
            down(dt);
        });

        tuplesToPromote.forEach(dt -> {
            tuples.remove(dt);
            up(dt);
        });

        if (inSkyline) {
            tuples.add(tuple);
        } else {
            if (removing) {
                tuples.remove(tuple);
                tuple.setAction(SkyAction.EVALUATION);
                down(tuple);
            } else {
                if (!evaluation) {
                    down(tuple);
                }
            }
        }

        if (removing || inSkyline || tuplesToDemote.size() > 0 || tuplesToPromote.size() > 0) {
            change();
        }

        return inSkyline;
    }

    public SkyStructure<T> getStructure() {
        return structure;
    }

    public int getDimensions() {
        return structure.getDimensions();
    }

    public List<T> getTuples() {
        return tuples;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getCount() {
        return tuples.size();
    }

    public void clear() {
        tuples.clear();
    }

    @Override
    public String toString() {
        return MapperUtility.serialize(this);
    }

}
