package com.skyline.utility;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.SkyStructure;
import com.skyline.model.BasicSkyTuple;

public class TupleFactoryTest {

    private final static int QUANTITY = 10;

    private static SkyStructure<BasicSkyTuple> STRUCTURE;

    static {
        try {
            STRUCTURE = StructureLoader.load("src/test/resources/structures/cars.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDefault() {
        Assert.assertNotNull(new TupleFactory());
    }

    @Test
    public void testGetterAndSetters() {
        TupleFactory.setExpiring(true);
        Assert.assertTrue(TupleFactory.isExpiring());
        TupleFactory.setMinDuration(0);
        Assert.assertEquals(0, TupleFactory.getMinDuration());
        TupleFactory.setMaxDuration(1000);
        Assert.assertEquals(1000, TupleFactory.getMaxDuration());
        TupleFactory.setDurationRange(1, 5);
        Assert.assertEquals(1, TupleFactory.getMinDuration());
        Assert.assertEquals(5, TupleFactory.getMaxDuration());
    }

    @Test
    public void testGenerateRandomAntiCorrelated() throws IOException {
        assertTuple(TupleFactory.generateRandomAntiCorrelated(STRUCTURE));
    }

    @Test
    public void testGenerateRandomCorrelated() throws IOException {
        assertTuple(TupleFactory.generateRandomCorrelated(STRUCTURE));
    }

    @Test
    public void testGenerateRandomIndependent() throws IOException {
        assertTuple(TupleFactory.generateRandomIndependent(STRUCTURE));
    }

    @Test
    public void testGenerateMultipleRandomAntiCorrelated() throws IOException {
        assertMultipleTuples(TupleFactory.generateMultipleRandomAntiCorrelated(STRUCTURE, QUANTITY));
    }

    @Test
    public void testGenerateMultipleRandomCorrelated() throws IOException {
        assertMultipleTuples(TupleFactory.generateMultipleRandomCorrelated(STRUCTURE, QUANTITY));
    }

    @Test
    public void testGenerateMultipleRandomIndependent() throws IOException {
        assertMultipleTuples(TupleFactory.generateMultipleRandomIndependent(STRUCTURE, QUANTITY));
    }

    private void assertMultipleTuples(List<BasicSkyTuple> tuples) {
        Assert.assertEquals(QUANTITY, tuples.size());
        tuples.forEach(tuple -> {
            assertTuple(tuple);
        });
    }

    private void assertTuple(BasicSkyTuple tuple) {
        Assert.assertNotNull(tuple.getIdentifier());
        Assert.assertNull(tuple.getDominate());
        Assert.assertNotNull(tuple.getCreated());
        if (TupleFactory.isExpiring()) {
            Assert.assertNotNull(tuple.getExpiration());
        } else {
            Assert.assertNull(tuple.getExpiration());
        }
    }

}
