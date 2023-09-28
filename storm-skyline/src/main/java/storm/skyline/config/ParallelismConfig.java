package storm.skyline.config;

public class ParallelismConfig {

    private int inputs;

    private int map;

    private int reduce;

    private int global;

    private int messaging;

    private int persistence;

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }

    public int getGlobal() {
        return global;
    }

    public void setGlobal(int global) {
        this.global = global;
    }

    public int getMessaging() {
        return messaging;
    }

    public void setMessaging(int messaging) {
        this.messaging = messaging;
    }

    public int getPersistence() {
        return persistence;
    }

    public void setPersistence(int persistence) {
        this.persistence = persistence;
    }

}
