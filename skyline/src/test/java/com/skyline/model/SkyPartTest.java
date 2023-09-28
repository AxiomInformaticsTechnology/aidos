package com.skyline.model;

import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.SkyPart.Result;

public class SkyPartTest {

    @Test
    public void testCreateDefault() {
        SkyPart part = new SkyPart();
        Assert.assertEquals(SkyPart.Bias.MIN, part.getBias());
        Assert.assertEquals(1, part.getScale());
        Assert.assertEquals(0, part.getBase());
    }

    @Test
    public void testCreateWithLabelAndBias() {
        SkyPart part = new SkyPart("Test", SkyPart.Bias.MAX);
        Assert.assertEquals("Test", part.getLabel());
        Assert.assertEquals(SkyPart.Bias.MAX, part.getBias());
    }

    @Test
    public void testCreateWithLabelAndBiasAndScale() {
        SkyPart part = new SkyPart("Test", SkyPart.Bias.MAX, 10);
        Assert.assertEquals("Test", part.getLabel());
        Assert.assertEquals(SkyPart.Bias.MAX, part.getBias());
        Assert.assertEquals(10, part.getScale());
    }

    @Test
    public void testCreateWithLabelAndBiasAndOptions() {
        Object[] options = new Object[] { "A", "B", "C" };
        SkyPart part = new SkyPart("Test", SkyPart.Bias.VALUE, options);
        Assert.assertEquals("Test", part.getLabel());
        Assert.assertEquals(SkyPart.Bias.VALUE, part.getBias());
        Assert.assertEquals(options[0], part.getOptions()[0]);
        Assert.assertEquals(options[1], part.getOptions()[1]);
        Assert.assertEquals(options[2], part.getOptions()[2]);
    }

    @Test
    public void testCreateWithLabelAndBiasAndScaleAndOptions() {
        Object[] options = new Object[] { "A", "B", "C" };
        SkyPart part = new SkyPart("Test", SkyPart.Bias.VALUE, 10, options);
        Assert.assertEquals("Test", part.getLabel());
        Assert.assertEquals(SkyPart.Bias.VALUE, part.getBias());
        Assert.assertEquals(10, part.getScale());
        Assert.assertEquals(options[0], part.getOptions()[0]);
        Assert.assertEquals(options[1], part.getOptions()[1]);
        Assert.assertEquals(options[2], part.getOptions()[2]);
    }

    @Test
    public void testCompare() {
        SkyPart part = new SkyPart();

        part.setBias(SkyPart.Bias.MIN);

        Assert.assertEquals(Result.BETTER, part.compare(0, 1));
        Assert.assertEquals(Result.WORSE, part.compare(1, 0));
        Assert.assertEquals(Result.EQUAL, part.compare(1, 1));

        Assert.assertEquals(Result.BETTER, part.compare(0.5, 1.5));
        Assert.assertEquals(Result.WORSE, part.compare(1.0, 0.5));
        Assert.assertEquals(Result.EQUAL, part.compare(1.25, 1.25));

        Assert.assertEquals(Result.BETTER, part.compare(1L, 2L));
        Assert.assertEquals(Result.WORSE, part.compare(2L, 0L));
        Assert.assertEquals(Result.EQUAL, part.compare(5L, 5L));

        part.setBias(SkyPart.Bias.MAX);

        Assert.assertEquals(Result.WORSE, part.compare(0, 1));
        Assert.assertEquals(Result.BETTER, part.compare(1, 0));
        Assert.assertEquals(Result.EQUAL, part.compare(1, 1));

        Assert.assertEquals(Result.WORSE, part.compare(0.5, 1.5));
        Assert.assertEquals(Result.BETTER, part.compare(1.0, 0.5));
        Assert.assertEquals(Result.EQUAL, part.compare(1.25, 1.25));

        Assert.assertEquals(Result.WORSE, part.compare(1L, 2L));
        Assert.assertEquals(Result.BETTER, part.compare(2L, 0L));
        Assert.assertEquals(Result.EQUAL, part.compare(5L, 5L));

        Object[] options = new Object[] { 1, 2, 3 };
        part.setBias(SkyPart.Bias.VALUE);
        part.setOptions(options);

        Assert.assertEquals(Result.BETTER, part.compare(1, 2));
        Assert.assertEquals(Result.WORSE, part.compare(3, 2));
        Assert.assertEquals(Result.EQUAL, part.compare(1, 1));

        part.setBias(SkyPart.Bias.VALUE);

        options = new Object[] { "A", "B", "C" };
        part.setOptions(options);

        Assert.assertEquals(Result.BETTER, part.compare("A", "B"));
        Assert.assertEquals(Result.WORSE, part.compare("C", "A"));
        Assert.assertEquals(Result.EQUAL, part.compare("C", "C"));

        options = new Object[] { 3L, 2L, 1L };
        part.setOptions(options);

        Assert.assertEquals(Result.BETTER, part.compare(2L, 1L));
        Assert.assertEquals(Result.WORSE, part.compare(2L, 3L));
        Assert.assertEquals(Result.EQUAL, part.compare(2L, 2L));

        options = new Object[] { 10.15, 12.5, 5.75 };
        part.setOptions(options);

        Assert.assertEquals(Result.BETTER, part.compare(10.15, 12.5));
        Assert.assertEquals(Result.WORSE, part.compare(5.75, 10.15));
        Assert.assertEquals(Result.EQUAL, part.compare(10.15, 10.15));
    }

}
