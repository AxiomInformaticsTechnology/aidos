package com.skyline.utility;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyPart;
import com.skyline.model.SkyStructure;

public class StructureLoaderTest {

    @Test
    public void testCreateDefault() {
        Assert.assertNotNull(new StructureLoader());
    }

    @Test
    public void testLoad() throws IOException {
        SkyStructure<BasicSkyTuple> structure = StructureLoader.load("src/test/resources/structures/cars.json");
        assertStructure(structure);
    }

    public static void assertStructure(SkyStructure<BasicSkyTuple> structure) {
        Assert.assertEquals("Cars", structure.getName());
        Assert.assertEquals(1, structure.getScale());
        Assert.assertEquals(4, structure.getParts().length);
        Assert.assertEquals(4, structure.getDimensions());

        Assert.assertEquals("Price", structure.getParts()[0].getLabel());
        Assert.assertEquals(SkyPart.Bias.MIN, structure.getParts()[0].getBias());
        Assert.assertEquals(75000, structure.getParts()[0].getScale());
        Assert.assertEquals(5000, structure.getParts()[0].getBase());

        Assert.assertEquals("HP", structure.getParts()[1].getLabel());
        Assert.assertEquals(SkyPart.Bias.MAX, structure.getParts()[1].getBias());
        Assert.assertEquals(425, structure.getParts()[1].getScale());
        Assert.assertEquals(75, structure.getParts()[1].getBase());

        Assert.assertEquals("MPG", structure.getParts()[2].getLabel());
        Assert.assertEquals(SkyPart.Bias.MAX, structure.getParts()[2].getBias());
        Assert.assertEquals(85, structure.getParts()[2].getScale());
        Assert.assertEquals(15, structure.getParts()[2].getBase());

        Assert.assertEquals("Color", structure.getParts()[3].getLabel());
        Assert.assertEquals(SkyPart.Bias.VALUE, structure.getParts()[3].getBias());
        Assert.assertEquals(1, structure.getParts()[3].getScale());
        Assert.assertEquals(0, structure.getParts()[3].getBase());
        Assert.assertEquals("RED", structure.getParts()[3].getOptions()[0]);
        Assert.assertEquals("GREEN", structure.getParts()[3].getOptions()[1]);
        Assert.assertEquals("BLUE", structure.getParts()[3].getOptions()[2]);
        Assert.assertEquals("YELLOW", structure.getParts()[3].getOptions()[3]);
        Assert.assertEquals("ORANGE", structure.getParts()[3].getOptions()[4]);
        Assert.assertEquals("WHITE", structure.getParts()[3].getOptions()[5]);
        Assert.assertEquals("BLACK", structure.getParts()[3].getOptions()[6]);
    }

}
