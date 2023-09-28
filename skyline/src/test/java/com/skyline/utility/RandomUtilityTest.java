package com.skyline.utility;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilityTest {

    private final static double MIN = -999.9;
    private final static double MAX = 999.9;
    private final static int DIM = 3;

    @Test
    public void testCreateDefault() {
        Assert.assertNotNull(new RandomUtility());
    }

    @Test
    public void testGetRandom() {
        Random random = RandomUtility.getRandom();
        Assert.assertNotNull(random);
    }

    @Test
    public void testRandomInteger() {
        int value = RandomUtility.randomInteger(-100, 100);
        Assert.assertTrue(value >= -100);
        Assert.assertTrue(value <= 100);
        boolean zero = false;
        boolean one = false;
        for (int i = 0; i < 100; i++) {
            value = RandomUtility.randomInteger(0, 1);
            Assert.assertTrue(value >= 0);
            Assert.assertTrue(value <= 1);
            if (value == 0) {
                zero = true;
            }
            if (value == 1) {
                one = true;
            }
            if (zero && one) {
                break;
            }
        }
        Assert.assertTrue(one);
        Assert.assertTrue(zero);
    }

    @Test
    public void testRandomBoolean() {
        boolean t = false;
        boolean f = false;
        for (int i = 0; i < 100; i++) {
            boolean value = RandomUtility.randomBoolean();
            if (value) {
                t = true;
            } else {
                f = true;
            }
            if (t && f) {
                break;
            }
        }
        Assert.assertTrue(t);
        Assert.assertTrue(f);
    }

    @Test
    public void testRandomEqual() {
        assertMinMax(RandomUtility.randomEqual(MIN, MAX));
    }

    @Test
    public void randomPeak() {
        assertMinMax(RandomUtility.randomPeak(MIN, MAX, DIM));
    }

    @Test
    public void randomNormal() {
        assertMinMax(RandomUtility.randomNormal(555.5, 0.5));
    }

    @Test
    public void testIsVectorOkTrue() {
        Assert.assertTrue(RandomUtility.isVectorOk(DIM, new double[] { 0.1, 0.7, 0.3 }));
    }

    @Test
    public void testIsVectorOkFalse() {
        Assert.assertFalse(RandomUtility.isVectorOk(DIM, new double[] { -0.1, 1.7, 0.3 }));
    }

    @Test
    public void testGenerateIndependent() {
        assertValues(RandomUtility.generateIndependent(DIM));
    }

    @Test
    public void testGenerateCorrelated() {
        assertValues(RandomUtility.generateCorrelated(DIM));
    }

    @Test
    public void testGenerateAntiCorrelated() {
        assertValues(RandomUtility.generateAntiCorrelated(DIM));
    }

    private void assertMinMax(double value) {
        Assert.assertTrue(value >= MIN);
        Assert.assertTrue(value <= MAX);
    }

    private void assertValues(double[] values) {
        Assert.assertEquals(DIM, values.length);
        for (int i = 0; i < DIM; i++) {
            Assert.assertTrue(values[i] > 0);
            Assert.assertTrue(values[i] < 1);
        }
    }

}
