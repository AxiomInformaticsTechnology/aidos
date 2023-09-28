package com.skyline;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyStructure;

public class BasicSkyline extends AbstractSkyline<BasicSkyTuple> {

    private static final long serialVersionUID = 4418420740566354948L;

    public BasicSkyline(SkyStructure<BasicSkyTuple> structure) {
        super(structure);
    }

    public BasicSkyline(SkyStructure<BasicSkyTuple> structure, Integer layer) {
        super(structure, layer);
    }

    @Override
    public void up(BasicSkyTuple tuple) {

    }

    @Override
    public void down(BasicSkyTuple tuple) {

    }

    @Override
    public void expire(BasicSkyTuple tuple) {

    }

    @Override
    public void change() {

    }

}
