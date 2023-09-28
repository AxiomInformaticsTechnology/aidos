package com.skyline.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SkyPart implements Serializable {

    private static final long serialVersionUID = -1718111374893303283L;

    @JsonInclude(Include.NON_NULL)
    private String label;

    private Bias bias;

    private int scale;

    private int base;

    @JsonInclude(Include.NON_NULL)
    private Object[] options;

    public SkyPart() {
        this.bias = Bias.MIN;
        this.scale = 1;
        this.base = 0;
    }

    public SkyPart(String label, Bias bias) {
        this();
        this.label = label;
        this.bias = bias;
    }

    public SkyPart(String label, Bias bias, int scale) {
        this(label, bias);
        this.scale = scale;
    }

    public SkyPart(String label, Bias bias, Object[] options) {
        this(label, bias);
        this.options = options;
    }

    public SkyPart(String label, Bias bias, int scale, Object[] options) {
        this(label, bias, options);
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Bias getBias() {
        return bias;
    }

    public void setBias(Bias bias) {
        this.bias = bias;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base1) {
        base = base1;
    }

    public Object[] getOptions() {
        return options;
    }

    public void setOptions(Object[] options) {
        this.options = options;
    }

    public static enum Bias {
        MAX, MIN, VALUE
    }

    public Result compare(Object v1, Object v2) {
        Result result = Result.EQUAL;
        if (bias.equals(Bias.VALUE)) {
            List<Object> options = Arrays.asList(this.options);
            int v1i = options.indexOf(v1);
            int v2i = options.indexOf(v2);
            if (v1i == v2i) {
                result = Result.EQUAL;
            } else {
                result = v1i < v2i ? Result.BETTER : Result.WORSE;
            }
        } else {
            if (v1 instanceof Integer && v2 instanceof Integer) {
                if (((Integer) v1).equals((Integer) v2)) {
                    result = Result.EQUAL;
                } else {
                    if (bias.equals(Bias.MIN)) {
                        result = (Integer) v1 < (Integer) v2 ? Result.BETTER : Result.WORSE;
                    } else {
                        result = (Integer) v1 > (Integer) v2 ? Result.BETTER : Result.WORSE;
                    }
                }
            } else if (v1 instanceof Long && v2 instanceof Long) {
                if (((Long) v1).equals((Long) v2)) {
                    result = Result.EQUAL;
                } else {
                    if (bias.equals(Bias.MIN)) {
                        result = (Long) v1 < (Long) v2 ? Result.BETTER : Result.WORSE;
                    } else {
                        result = (Long) v1 > (Long) v2 ? Result.BETTER : Result.WORSE;
                    }
                }
            } else if (v1 instanceof Double && v2 instanceof Double) {
                if (((Double) v1).equals((Double) v2)) {
                    result = Result.EQUAL;
                } else {
                    if (bias.equals(Bias.MIN)) {
                        result = (Double) v1 < (Double) v2 ? Result.BETTER : Result.WORSE;
                    } else {
                        result = (Double) v1 > (Double) v2 ? Result.BETTER : Result.WORSE;
                    }
                }
            }
        }
        return result;
    }

    public static enum Result {
        BETTER, EQUAL, WORSE
    }

}