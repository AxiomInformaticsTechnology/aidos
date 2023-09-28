package com.skyline.model;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SkyStructureTest {

    @Test
    public void testCreateDefault() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>();
        Assert.assertEquals(1, structure.getScale());
    }

    @Test
    public void testCreateWithName() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>("Test");
        Assert.assertEquals("Test", structure.getName());
    }

    @Test
    public void testCreateWithNameAndScale() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>("Test", 2);
        Assert.assertEquals("Test", structure.getName());
        Assert.assertEquals(2, structure.getScale());
    }

    @Test
    public void testToString() throws JsonProcessingException {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>();
        Assert.assertNotNull(structure.toString());
    }

}
