package com.skyline.utility;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.BasicSkyTuple;

public class TupleLoaderTest {

    @Test
    public void testCreateDefault() {
        Assert.assertNotNull(new TupleLoader());
    }

    @Test
    public void testLoadCarsIndependent() throws IOException {
        assertLoadTuples("src/test/resources/tuples/cars-independent-1000.json", 1000, 4);
    }

    @Test
    public void testLoadCarsCorrelated() throws IOException {
        assertLoadTuples("src/test/resources/tuples/cars-correlated-1000.json", 1000, 4);
    }

    @Test
    public void testLoadCarsAntiCorrelated() throws IOException {
        assertLoadTuples("src/test/resources/tuples/cars-anticorrelated-1000.json", 1000, 4);
    }

    @Test
    public void testLoadHotelsIndependent() throws IOException {
        assertLoadTuples("src/test/resources/tuples/hotels-independent-100.json", 100, 2);
    }

    @Test
    public void testLoadHotelsCorrelated() throws IOException {
        assertLoadTuples("src/test/resources/tuples/hotels-correlated-100.json", 100, 2);
    }

    @Test
    public void testLoadHotelsAntiCorrelated() throws IOException {
        assertLoadTuples("src/test/resources/tuples/hotels-anticorrelated-100.json", 100, 2);
    }

    @Test
    public void testLoadIndependent2dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-independent-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadCorrelated2dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-correlated-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadAntiCorrelated2dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-anticorrelated-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadIndependent2dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-independent-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadCorrelated2dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-correlated-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadAntiCorrelated2dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-anticorrelated-2dmax10000.json", 10000, 2);
    }

    @Test
    public void testLoadIndependent3dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-independent-3dmax5000.json", 5000, 3);
    }

    @Test
    public void testLoadCorrelated3dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-correlated-3dmax5000.json", 5000, 3);
    }

    @Test
    public void testLoadAntiCorrelated3dmax() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-anticorrelated-3dmax5000.json", 5000, 3);
    }

    @Test
    public void testLoadIndependent3dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-independent-3dmax5000.json", 5000, 3);
    }

    @Test
    public void testLoadCorrelated3dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-correlated-3dmax5000.json", 5000, 3);
    }

    @Test
    public void testLoadAntiCorrelated3dmin() throws IOException {
        assertLoadTuples("src/test/resources/tuples/test-anticorrelated-3dmax5000.json", 5000, 3);
    }

    private void assertLoadTuples(String path, int expectedNumberOfTuples, int expectedDimensions) throws IOException {
        List<BasicSkyTuple> tuples = TupleLoader.load(path);
        Assert.assertEquals(expectedNumberOfTuples, tuples.size());
        tuples.forEach(tuple -> {
            Assert.assertEquals(expectedDimensions, tuple.getValues().length);
            Assert.assertNotNull(tuple.getIdentifier());
            Assert.assertNotNull(tuple.getCreated());
        });
    }

}
