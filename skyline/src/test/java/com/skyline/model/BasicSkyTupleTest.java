package com.skyline.model;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class BasicSkyTupleTest {

    @Test
    public void testCreateDefault() {
        BasicSkyTuple tuple = new BasicSkyTuple();
        Assert.assertNotNull(tuple.getIdentifier());
        Assert.assertNotNull(tuple.getCreated());
        Assert.assertEquals(SkyAction.INSERTION, tuple.getAction());
        Assert.assertEquals(0, tuple.getLayer());
        Assert.assertEquals(tuple.getIdentifier().hashCode(), tuple.hashCode());
    }

    @Test
    public void testCreateWithValues() {
        Object[] values = new Object[] { 1, 2, 3 };
        BasicSkyTuple tuple = new BasicSkyTuple(values);
        Assert.assertEquals(values[0], tuple.getValues()[0]);
        Assert.assertEquals(values[1], tuple.getValues()[1]);
        Assert.assertEquals(values[2], tuple.getValues()[2]);
    }

    @Test
    public void testCreateWithValuesAndExpiration() {
        Object[] values = new Object[] { 1, 2, 3 };
        Long expiration = Instant.now().toEpochMilli();
        BasicSkyTuple tuple = new BasicSkyTuple(values, expiration);
        Assert.assertEquals(values[0], tuple.getValues()[0]);
        Assert.assertEquals(values[1], tuple.getValues()[1]);
        Assert.assertEquals(values[2], tuple.getValues()[2]);
        Assert.assertEquals(expiration, tuple.getExpiration());
    }

    @Test
    public void testCreateWithValuesAndIdentifier() {
        Object[] values = new Object[] { 1, 2, 3 };
        BasicSkyTuple tuple = new BasicSkyTuple(values, "Test");
        Assert.assertEquals(values[0], tuple.getValues()[0]);
        Assert.assertEquals(values[1], tuple.getValues()[1]);
        Assert.assertEquals(values[2], tuple.getValues()[2]);
        Assert.assertEquals("Test", tuple.getIdentifier());
    }

    @Test
    public void testCreateWithValuesAndIdentifierAndExpiration() {
        Object[] values = new Object[] { 1, 2, 3 };
        Long expiration = Instant.now().toEpochMilli();
        BasicSkyTuple tuple = new BasicSkyTuple(values, "Test", expiration);
        Assert.assertEquals(values[0], tuple.getValues()[0]);
        Assert.assertEquals(values[1], tuple.getValues()[1]);
        Assert.assertEquals(values[2], tuple.getValues()[2]);
        Assert.assertEquals("Test", tuple.getIdentifier());
        Assert.assertEquals(expiration, tuple.getExpiration());
    }

    @Test
    public void testLayerUpAndDown() {
        BasicSkyTuple tuple = new BasicSkyTuple();
        tuple.setLayer(1);
        Assert.assertEquals(1, tuple.getLayer());
        tuple.layerDown();
        Assert.assertEquals(2, tuple.getLayer());
        tuple.layerUp();
        Assert.assertEquals(1, tuple.getLayer());
    }

    @Test
    public void testToString() throws JsonProcessingException {
        BasicSkyTuple tuple = new BasicSkyTuple();
        Assert.assertNotNull(tuple.toString());
    }

}
