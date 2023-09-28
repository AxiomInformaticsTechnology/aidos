package com.skyline.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.skyline.model.SkyPart.Result;
import com.skyline.utility.MapperUtility;

public class SkyStructure<T extends SkyTuple> implements Serializable {

    private static final long serialVersionUID = -3754107037747516089L;

    @JsonInclude(Include.NON_NULL)
    private String name;

    private int scale;

    private SkyPart[] parts;

    public SkyStructure() {
        this.scale = 1;
        this.parts = new SkyPart[0];
    }

    public SkyStructure(String name) {
        this();
        this.name = name;
    }

    public SkyStructure(String name, int scale) {
        this(name);
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public SkyPart[] getParts() {
        return parts;
    }

    public void setParts(SkyPart[] parts) {
        this.parts = parts;
    }

    public int getDimensions() {
        return parts.length;
    }

    public boolean dominates(T t1, T t2) {
        Object[] t1Values = t1.getValues();
        Object[] t2Values = t2.getValues();
        boolean tupleBetterOrEqualOnAllDimensions = true;
        boolean tupleBetterOnOneDimension = false;
        for (int i = 0; i < getDimensions(); i++) {
            Result result = parts[i].compare(t1Values[i], t2Values[i]);
            if (result.equals(Result.WORSE)) {
                tupleBetterOrEqualOnAllDimensions = false;
            } else {
                if (result.equals(Result.BETTER)) {
                    tupleBetterOnOneDimension = true;
                }
            }
            if (!tupleBetterOrEqualOnAllDimensions) {
                break;
            }
        }
        return tupleBetterOrEqualOnAllDimensions && tupleBetterOnOneDimension;
    }

    @Override
    public String toString() {
        return MapperUtility.serialize(this);
    }

}
