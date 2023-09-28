package com.skyline.utility;

import java.util.Random;

public class RandomUtility {

    private final static int RANDOM_MAX = 32767891;

    private final static Random random = new Random();;

    public static Random getRandom() {
        return random;
    }

    public static Boolean randomBoolean() {
        return randomInteger(0, 1) == 1 ? true : false;
    }

    public static int randomInteger(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static double randomEqual(double min, double max) {
        double x = (double) random.nextInt(RANDOM_MAX) / RANDOM_MAX;
        return x * (max - min) + min;
    }

    public static double randomPeak(double min, double max, int dim) {
        double sum = 0.0;
        for (int d = 0; d < dim; d++) {
            sum += randomEqual(0, 1);
        }
        sum /= dim;
        return sum * (max - min) + min;
    }

    public static double randomNormal(double med, double var) {
        return randomPeak(med - var, med + var, 12);
    }

    public static boolean isVectorOk(int dim, double[] x) {
        for (int i = 0; i < dim; i++) {
            if (x[i] < 0.0 || x[i] > 1.0) {
                return false;
            }
        }
        return true;
    }

    public static double[] generateRandom(int dim, DataType type) {
        double[] values = new double[dim];
        switch (type) {
        case ANTICORRELATED:
            values = generateAntiCorrelated(dim);
            break;
        case CORRELATED:
            values = generateCorrelated(dim);
            break;
        case INDEPENDENT:
            values = generateIndependent(dim);
            break;
        }
        return values;
    }

    public static double[] generateIndependent(int dim) {
        double[] x = new double[dim];
        for (int d = 0; d < dim; d++) {
            x[d] = randomEqual(0, 1);
        }
        return x;
    }

    public static double[] generateCorrelated(int dim) {
        double[] x = new double[dim];
        do {
            int d;
            double v = randomPeak(0, 1, dim);
            double l = v <= 0.5 ? v : 1.0 - v;
            for (d = 0; d < dim; d++) {
                x[d] = v;
            }
            for (d = 0; d < dim; d++) {
                double h = randomNormal(0, l);
                x[d] += h;
                x[(d + 1) % dim] -= h;
            }
        } while (!isVectorOk(dim, x));
        return x;
    }

    public static double[] generateAntiCorrelated(int dim) {
        double[] x = new double[dim];
        do {
            int d;
            double v = randomNormal(0.5, 0.25);
            double l = v <= 0.5 ? v : 1.0 - v;
            for (d = 0; d < dim; d++) {
                x[d] = v;
            }
            for (d = 0; d < dim; d++) {
                double h = randomEqual(-l, l);
                x[d] += h;
                x[(d + 1) % dim] -= h;
            }
        } while (!isVectorOk(dim, x));
        return x;
    }

    public static enum DataType {
        INDEPENDENT, CORRELATED, ANTICORRELATED
    }

}
