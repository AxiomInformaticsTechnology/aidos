package com.skyline;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.skyline.h2.H2;
import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyStructure;
import com.skyline.utility.TupleLoader;

public abstract class AbstractSkylineTest {

    protected final static Logger logger = Logger.getLogger(AbstractSkylineTest.class);

    protected abstract Skyline<BasicSkyTuple> loadSkyline(String path) throws IOException;

    protected List<BasicSkyTuple> loadTuples(String path) throws IOException {
        return TupleLoader.load(path);
    }

    protected void setup(SkyStructure<BasicSkyTuple> structure, List<BasicSkyTuple> tuples) throws ClassNotFoundException, SQLException {
        H2.delete();
        H2.createTable(structure);
        H2.insertTuples(structure, tuples);
    }

    protected void process(Skyline<BasicSkyTuple> skyline, List<BasicSkyTuple> tuples) {
        tuples.forEach(tuple -> {
            skyline.in(tuple);
        });
    }

    protected List<BasicSkyTuple> h2SkylineQuery(SkyStructure<BasicSkyTuple> structure) throws ClassNotFoundException, SQLException {
        return H2.skylineQuery(structure);
    }

    protected void cleanup() {
        H2.delete();
    }

    protected void assertSkylineTuples(List<BasicSkyTuple> expectedTuples, Skyline<BasicSkyTuple> skyline) {
        Assert.assertEquals(0, skyline.getLayer());
        SkyStructure<BasicSkyTuple> structure = skyline.getStructure();
        expectedTuples.forEach(expectedTuple -> {
            String identifier = expectedTuple.getIdentifier();
            Object[] expectedValues = expectedTuple.getValues();
            for (BasicSkyTuple tuple : skyline.getTuples()) {
                if (tuple.getIdentifier().equals(identifier)) {
                    Assert.assertEquals(identifier, tuple.getIdentifier());
                    Object[] values = tuple.getValues();
                    for (int i = 0; i < structure.getDimensions(); i++) {
                        Assert.assertEquals(values[i], expectedValues[i]);
                    }
                    break;
                }
            }
        });
    }

    protected void assertTest(Skyline<BasicSkyTuple> skyline, List<BasicSkyTuple> tuples) throws ClassNotFoundException, SQLException {
        SkyStructure<BasicSkyTuple> structure = skyline.getStructure();

        setup(structure, tuples);

        process(skyline, tuples);

        assertSkylineTuples(h2SkylineQuery(structure), skyline);

        // NOTE: in this test case all sub skyline tuples have dominate of tuple in skyline
        assertSubSkylineDominateTuples(skyline, tuples);

        cleanup();

        Assert.assertEquals(2, skyline.getTuples().size());

        logger.info("Skyline: " + skyline);
    }

    protected void updateTupleExpiration(List<BasicSkyTuple> tuples, long duration, double delta) {
        tuples.forEach(tuple -> {
            for (Object value : tuple.getValues()) {
                value = (double) value + delta;
            }
            tuple.setExpiration((duration * 1000) + Instant.now().toEpochMilli());
        });
    }

    protected void assertSubSkylineDominateTuples(Skyline<BasicSkyTuple> skyline, List<BasicSkyTuple> tuples) {
        List<BasicSkyTuple> skylineTuples = skyline.getTuples();
        tuples.forEach(tuple -> {
            Optional<String> dominate = Optional.ofNullable(tuple.getDominate());
            if (dominate.isPresent()) {
                boolean dominateInSkyline = false;
                for (BasicSkyTuple skylineTuple : skylineTuples) {
                    if (dominate.get().equals(skylineTuple.getIdentifier())) {
                        dominateInSkyline = true;
                        break;
                    }
                }
                Assert.assertTrue(dominateInSkyline);
            } else {
                Assert.assertTrue(skylineTuples.contains(tuple));
            }
        });
    }

    protected Skyline<BasicSkyTuple> testSkyline(String structurePath, String tuplesPath) throws IOException, ClassNotFoundException, SQLException {
        Skyline<BasicSkyTuple> skyline = loadSkyline(structurePath);
        List<BasicSkyTuple> tuples = loadTuples(tuplesPath);

        SkyStructure<BasicSkyTuple> structure = skyline.getStructure();

        setup(structure, tuples);

        process(skyline, tuples);

        assertSkylineTuples(h2SkylineQuery(structure), skyline);

        cleanup();

        logger.info("Skyline: " + skyline);

        return skyline;
    }

}
