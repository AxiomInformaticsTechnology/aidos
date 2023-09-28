package storm.skyline.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TopologyConfig {

    private String name;

    private String structurePath;

    private boolean debug;

    private boolean timer;

    private int spouts;

    private int workers;

    private ParallelismConfig parallelism;

    private QueueConfig updateQueue;

    private QueueConfig outputQueue;

    @JsonInclude(Include.NON_NULL)
    private FileSpoutConfig fileSpoutConfig;

    private RandomSpoutConfig randomSpoutConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructurePath() {
        return structurePath;
    }

    public void setStructurePath(String structurePath) {
        this.structurePath = structurePath;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isTimer() {
        return timer;
    }

    public void setTimer(boolean timer) {
        this.timer = timer;
    }

    public int getSpouts() {
        return spouts;
    }

    public void setSpouts(int spouts) {
        this.spouts = spouts;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public ParallelismConfig getParallelism() {
        return parallelism;
    }

    public void setParallelism(ParallelismConfig parallelism) {
        this.parallelism = parallelism;
    }

    public QueueConfig getUpdateQueue() {
        return updateQueue;
    }

    public void setUpdateQueue(QueueConfig updateQueue) {
        this.updateQueue = updateQueue;
    }

    public QueueConfig getOutputQueue() {
        return outputQueue;
    }

    public void setOutputQueue(QueueConfig outputQueue) {
        this.outputQueue = outputQueue;
    }

    public FileSpoutConfig getFileSpoutConfig() {
        return fileSpoutConfig;
    }

    public void setFileSpoutConfig(FileSpoutConfig fileSpoutConfig) {
        this.fileSpoutConfig = fileSpoutConfig;
    }

    public RandomSpoutConfig getRandomSpoutConfig() {
        return randomSpoutConfig;
    }

    public void setRandomSpoutConfig(RandomSpoutConfig randomSpoutConfig) {
        this.randomSpoutConfig = randomSpoutConfig;
    }

    public boolean isFileSpout() {
        return fileSpoutConfig != null && fileSpoutConfig.getPath() != null;
    }

}
