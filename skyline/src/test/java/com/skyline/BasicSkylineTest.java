package com.skyline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyStructure;
import com.skyline.utility.StructureLoader;

public class BasicSkylineTest extends AbstractSkylineTest {

    @Test
    public void testCreateWithStructure() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>("Test");
        BasicSkyline skyline = new BasicSkyline(structure);
        Assert.assertNotNull(skyline);
        Assert.assertEquals("Test", skyline.getStructure().getName());
        Assert.assertEquals(0, skyline.getStructure().getDimensions());
    }

    @Test
    public void testCreateWithStructureAndLayer() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>("Test");
        BasicSkyline skyline = new BasicSkyline(structure, 10);
        Assert.assertNotNull(skyline);
        Assert.assertEquals("Test", skyline.getStructure().getName());
        Assert.assertEquals(0, skyline.getStructure().getDimensions());
        Assert.assertEquals(10, skyline.getLayer());
    }

    @Test
    public void testSetLayer() {
        SkyStructure<BasicSkyTuple> structure = new SkyStructure<BasicSkyTuple>("Test");
        BasicSkyline skyline = new BasicSkyline(structure);
        skyline.setLayer(5);
        Assert.assertEquals(5, skyline.getLayer());
    }

    @Test
    public void testBasic() throws IOException, ClassNotFoundException, SQLException {
        BasicSkyline skyline = loadSkyline("src/test/resources/structures/basic.json");
        List<BasicSkyTuple> tuples = loadTuples("src/test/resources/tuples/basic.json");
        assertTest(skyline, tuples);
        skyline.up(new BasicSkyTuple());
    }

    @Test
    public void testClear() throws IOException, ClassNotFoundException, SQLException {
        BasicSkyline skyline = loadSkyline("src/test/resources/structures/basic.json");
        List<BasicSkyTuple> tuples = loadTuples("src/test/resources/tuples/basic.json");
        assertTest(skyline, tuples);
        skyline.clear();
        Assert.assertEquals(0, skyline.getTuples().size());
    }

    @Test
    public void testDuration() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        BasicSkyline skyline = loadSkyline("src/test/resources/structures/basic.json");

        List<BasicSkyTuple> tenSecondTuples = loadTuples("src/test/resources/tuples/basic.json");
        updateTupleExpiration(tenSecondTuples, 1, 0.0);

        assertTest(skyline, tenSecondTuples);

        // NOTE: will allow current skyline tuples to expire and be removed next process
        Thread.sleep(1000);

        List<BasicSkyTuple> thirtySecondTuples = loadTuples("src/test/resources/tuples/basic.json");
        updateTupleExpiration(thirtySecondTuples, 2, 0.1);

        assertTest(skyline, thirtySecondTuples);
    }

    @Test
    public void testIndependent2dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmax.json", "src/test/resources/tuples/test-independent-2dmax10000.json");
        Assert.assertEquals(13, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelated2dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmax.json", "src/test/resources/tuples/test-anticorrelated-2dmax10000.json");
        Assert.assertEquals(41, skyline.getTuples().size());
    }

    @Test
    public void testCorrelated2dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmax.json", "src/test/resources/tuples/test-correlated-2dmax10000.json");
        Assert.assertEquals(3, skyline.getTuples().size());
    }

    @Test
    public void testIndependent2dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmin.json", "src/test/resources/tuples/test-independent-2dmin10000.json");
        Assert.assertEquals(8, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelated2dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmin.json", "src/test/resources/tuples/test-anticorrelated-2dmin10000.json");
        Assert.assertEquals(32, skyline.getTuples().size());
    }

    @Test
    public void testCorrelated2dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test2dmin.json", "src/test/resources/tuples/test-correlated-2dmin10000.json");
        Assert.assertEquals(2, skyline.getTuples().size());
    }

    @Test
    public void testIndependent3dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmax.json", "src/test/resources/tuples/test-independent-3dmax5000.json");
        Assert.assertEquals(37, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelated3dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmax.json", "src/test/resources/tuples/test-anticorrelated-3dmax5000.json");
        Assert.assertEquals(309, skyline.getTuples().size());
    }

    @Test
    public void testCorrelated3dmax() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmax.json", "src/test/resources/tuples/test-correlated-3dmax5000.json");
        Assert.assertEquals(3, skyline.getTuples().size());
    }

    @Test
    public void testIndependent3dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmin.json", "src/test/resources/tuples/test-independent-3dmin5000.json");
        Assert.assertEquals(39, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelated3dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmin.json", "src/test/resources/tuples/test-anticorrelated-3dmin5000.json");
        Assert.assertEquals(347, skyline.getTuples().size());
    }

    @Test
    public void testCorrelated3dmin() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/test3dmin.json", "src/test/resources/tuples/test-correlated-3dmin5000.json");
        Assert.assertEquals(5, skyline.getTuples().size());
    }

    @Test
    public void testIndependentCars() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/cars.json", "src/test/resources/tuples/cars-independent-1000.json");
        Assert.assertEquals(56, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelatedCars() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/cars.json", "src/test/resources/tuples/cars-anticorrelated-1000.json");
        Assert.assertEquals(38, skyline.getTuples().size());
    }

    @Test
    public void testCorrelatedCars() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/cars.json", "src/test/resources/tuples/cars-correlated-1000.json");
        Assert.assertEquals(108, skyline.getTuples().size());
    }

    @Test
    public void testIndependentHotels() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/hotels.json", "src/test/resources/tuples/hotels-independent-100.json");
        Assert.assertEquals(7, skyline.getTuples().size());
    }

    @Test
    public void testAntiCorrelatedHotels() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/hotels.json", "src/test/resources/tuples/hotels-anticorrelated-100.json");
        Assert.assertEquals(20, skyline.getTuples().size());
    }

    @Test
    public void testCorrelatedHotels() throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = testSkyline("src/test/resources/structures/hotels.json", "src/test/resources/tuples/hotels-correlated-100.json");
        Assert.assertEquals(2, skyline.getTuples().size());
    }

    @Override
    protected BasicSkyline loadSkyline(String path) throws IOException {
        return new BasicSkyline(StructureLoader.load(path));
    }

}
