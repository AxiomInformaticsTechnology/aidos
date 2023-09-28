package com.skyline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyAction;
import com.skyline.model.SkyStructure;
import com.skyline.utility.StructureLoader;

public class EventSkylineTest extends AbstractSkylineTest {

    private static int numberOfChanges;

    private static final List<BasicSkyTuple> tuplesPromoted = new ArrayList<BasicSkyTuple>();

    private static final List<BasicSkyTuple> tuplesDemoted = new ArrayList<BasicSkyTuple>();

    private static final List<BasicSkyTuple> tuplesExpired = new ArrayList<BasicSkyTuple>();

    @Before
    public void setup() {
        numberOfChanges = 0;
    }

    @Test
    public void testEventSkyline() throws IOException, ClassNotFoundException, SQLException {
        EventSkyline<BasicSkyTuple> skyline = loadSkyline("src/test/resources/structures/basic.json");
        List<BasicSkyTuple> tuples = loadTuples("src/test/resources/tuples/basic.json");
        assertTest(skyline, tuples);
        Assert.assertEquals(3, numberOfChanges);
        Assert.assertEquals(0, tuplesPromoted.size());
        Assert.assertEquals(8, tuplesDemoted.size());

        SkyStructure<BasicSkyTuple> structure = skyline.getStructure();
        setup(structure, tuples);

        tuples.removeAll(h2SkylineQuery(structure));

        Assert.assertEquals(tuples, tuplesDemoted);

        BasicSkyTuple tupleToRemove = skyline.getTuples().get(0);

        tupleToRemove.setAction(SkyAction.REMOVAL);

        skyline.in(tupleToRemove);

        Assert.assertEquals(4, numberOfChanges);

        Assert.assertFalse(skyline.getTuples().contains(tupleToRemove));

        Optional<BasicSkyTuple> tupleToAddAndBePromote = Optional.empty();
        for (BasicSkyTuple tuple : tuples) {
            if (tuple.getDominate().equals(tupleToRemove.getIdentifier())) {
                tupleToAddAndBePromote = Optional.of(tuple);
                break;
            }
        }

        Assert.assertTrue(tupleToAddAndBePromote.isPresent());

        skyline.in(tupleToAddAndBePromote.get());

        Assert.assertEquals(5, numberOfChanges);

        tupleToRemove.setAction(SkyAction.EVALUATION);

        skyline.in(tupleToRemove);

        Assert.assertEquals(6, numberOfChanges);
        Assert.assertEquals(1, tuplesPromoted.size());
        Assert.assertEquals(tupleToAddAndBePromote.get(), tuplesPromoted.get(0));

        BasicSkyTuple tupleToExpire = skyline.getTuples().get(0);

        tupleToExpire.setExpiration(tupleToExpire.getCreated());

        skyline.in(tupleToExpire);

        Assert.assertEquals(7, numberOfChanges);
        Assert.assertEquals(tupleToExpire, tuplesExpired.get(0));

        Assert.assertEquals(0, skyline.getTuples().size());
    }

    @Test
    public void testGetListener() throws IOException {
        EventSkyline<BasicSkyTuple> skyline = loadSkyline("src/test/resources/structures/basic.json");
        Assert.assertNotNull(skyline.getListener());
    }

    @Override
    protected EventSkyline<BasicSkyTuple> loadSkyline(String path) throws IOException {
        return new EventSkyline<BasicSkyTuple>(StructureLoader.load(path), new TupleListener<BasicSkyTuple>() {

            @Override
            public void up(BasicSkyTuple tuple) {
                tuplesPromoted.add(tuple);
            }

            @Override
            public void down(BasicSkyTuple tuple) {
                tuplesDemoted.add(tuple);
            }

            @Override
            public void expire(BasicSkyTuple tuple) {
                tuplesExpired.add(tuple);
            }

            @Override
            public void change() {
                numberOfChanges++;
            }

        });
    }

}
