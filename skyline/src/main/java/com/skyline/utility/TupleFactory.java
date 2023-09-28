package com.skyline.utility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyPart;
import com.skyline.model.SkyStructure;
import com.skyline.utility.RandomUtility.DataType;

public class TupleFactory {

    private static boolean expiring = false;

    private static int minDuration = 10;

    private static int maxDuration = 120;

    public static BasicSkyTuple generateRandomAntiCorrelated(SkyStructure<BasicSkyTuple> structure) {
        return generateRandom(structure, DataType.ANTICORRELATED);
    }

    public static BasicSkyTuple generateRandomCorrelated(SkyStructure<BasicSkyTuple> structure) {
        return generateRandom(structure, DataType.CORRELATED);
    }

    public static BasicSkyTuple generateRandomIndependent(SkyStructure<BasicSkyTuple> structure) {
        return generateRandom(structure, DataType.INDEPENDENT);
    }

    public static List<BasicSkyTuple> generateMultipleRandomAntiCorrelated(SkyStructure<BasicSkyTuple> structure, int quantity) {
        return generateMultipleRandom(structure, DataType.ANTICORRELATED, quantity);
    }

    public static List<BasicSkyTuple> generateMultipleRandomCorrelated(SkyStructure<BasicSkyTuple> structure, int quantity) {
        return generateMultipleRandom(structure, DataType.CORRELATED, quantity);
    }

    public static List<BasicSkyTuple> generateMultipleRandomIndependent(SkyStructure<BasicSkyTuple> structure, int quantity) {
        return generateMultipleRandom(structure, DataType.INDEPENDENT, quantity);
    }

    public static List<BasicSkyTuple> generateMultipleRandom(SkyStructure<BasicSkyTuple> structure, DataType type, int quantity) {
        List<BasicSkyTuple> tuples = new ArrayList<BasicSkyTuple>();
        for (int i = 0; i < quantity; i++) {
            tuples.add(generateRandom(structure, type));
        }
        return tuples;
    }

    public static BasicSkyTuple generateRandom(SkyStructure<BasicSkyTuple> structure, DataType type) {
        int structureScale = structure.getScale();
        int dimensions = structure.getDimensions();
        Object[] values = new Object[dimensions];
        double[] randomValues = RandomUtility.generateRandom(dimensions, type);
        SkyPart[] parts = structure.getParts();
        for (int i = 0; i < dimensions; i++) {
            SkyPart part = parts[i];
            Object value;
            switch (part.getBias()) {
            case VALUE:
                Object[] options = part.getOptions();
                value = options[(int) Math.floor(randomValues[i] * options.length)];
                break;
            case MAX:
            case MIN:
            default:
                value = part.getBase() + (randomValues[i] * structureScale * part.getScale());
                break;
            }
            values[i] = value;
        }
        BasicSkyTuple tuple;
        if (expiring) {
            int random = RandomUtility.randomInteger(minDuration, maxDuration);
            long expiration = (random * 1000) + Instant.now().toEpochMilli();
            tuple = new BasicSkyTuple(values, expiration);
        } else {
            tuple = new BasicSkyTuple(values);
        }
        return tuple;
    }

    public static boolean isExpiring() {
        return expiring;
    }

    public static void setExpiring(boolean expiring) {
        TupleFactory.expiring = expiring;
    }

    public static int getMinDuration() {
        return minDuration;
    }

    public static void setMinDuration(int minDuration) {
        TupleFactory.minDuration = minDuration;
    }

    public static int getMaxDuration() {
        return maxDuration;
    }

    public static void setMaxDuration(int maxDuration) {
        TupleFactory.maxDuration = maxDuration;
    }

    public static void setDurationRange(int min, int max) {
        TupleFactory.minDuration = min;
        TupleFactory.maxDuration = max;
    }

}
