package storm.skyline.config;

import java.io.Serializable;

import com.skyline.utility.RandomUtility.DataType;

public class RandomSpoutConfig implements Serializable {

    private static final long serialVersionUID = -2402792376069931681L;

    private DataType type;

    private int tupleDurationMin;

    private int tupleDurationMax;

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public int getTupleDurationMin() {
        return tupleDurationMin;
    }

    public void setTupleDurationMin(int tupleDurationMin) {
        this.tupleDurationMin = tupleDurationMin;
    }

    public int getTupleDurationMax() {
        return tupleDurationMax;
    }

    public void setTupleDurationMax(int tupleDurationMax) {
        this.tupleDurationMax = tupleDurationMax;
    }

}
