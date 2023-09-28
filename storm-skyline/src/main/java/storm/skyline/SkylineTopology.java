package storm.skyline;

import java.io.File;
import java.io.IOException;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.skyline.model.SkyStructure;
import com.skyline.utility.StructureLoader;

import storm.skyline.bolt.InputSkylineBolt;
import storm.skyline.bolt.MapSkylineBolt;
import storm.skyline.bolt.MessageSkylineBolt;
import storm.skyline.bolt.ReduceSkylineBolt;
import storm.skyline.bolt.SkylineBolt;
import storm.skyline.bolt.StoreSkylineBolt;
import storm.skyline.config.ParallelismConfig;
import storm.skyline.config.TopologyConfig;
import storm.skyline.messaging.SkylineBroker;
import storm.skyline.model.StormSkyTuple;
import storm.skyline.spout.FileSkylineSpout;
import storm.skyline.spout.RandomSkylineSpout;

public class SkylineTopology {

    public static void main(String[] args) throws Exception {

        try {
            SkylineBroker.initialize();

            runTopology("src/main/resources/config/basic-topology.yaml");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void runTopology(String topologyConfigPath) throws JsonParseException, JsonMappingException, IOException, AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        TopologyConfig topologyConfig = loadTopologyConfig(topologyConfigPath);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topologyConfig.getName(), buildConfig(topologyConfig), buildTopology(topologyConfig));
    }

    public static void submitTopology(String topologyConfigPath) throws JsonParseException, JsonMappingException, IOException, AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        TopologyConfig topologyConfig = loadTopologyConfig(topologyConfigPath);
        StormSubmitter.submitTopology(topologyConfig.getName(), buildConfig(topologyConfig), buildTopology(topologyConfig));
    }

    public static TopologyConfig loadTopologyConfig(String path) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(path), TopologyConfig.class);
    }

    public static Config buildConfig(TopologyConfig topologyConfig) {
        Config config = new Config();
        config.setDebug(topologyConfig.isDebug());
        config.registerSerialization(StormSkyTuple.class);
        config.setNumWorkers(topologyConfig.getWorkers());
        return config;
    }

    public static StormTopology buildTopology(TopologyConfig topologyConfig) throws IOException {
        SkyStructure<StormSkyTuple> structure = StructureLoader.load(topologyConfig.getStructurePath());

        InputSkylineBolt inputSkylineBolt = new InputSkylineBolt();
        MapSkylineBolt mapSkylineBolt = new MapSkylineBolt(topologyConfig.getUpdateQueue(), structure);
        ReduceSkylineBolt reduceSkylineBolt = new ReduceSkylineBolt(topologyConfig.getUpdateQueue(), structure);
        SkylineBolt skylineBolt = new SkylineBolt(topologyConfig.getUpdateQueue(), structure);
        StoreSkylineBolt storeSkylineBolt = new StoreSkylineBolt();
        MessageSkylineBolt messageSkylineBolt = new MessageSkylineBolt(topologyConfig.getOutputQueue());

        ParallelismConfig parallelism = topologyConfig.getParallelism();

        // @formatter:off
        TopologyBuilder builder = new TopologyBuilder();
        
        if(topologyConfig.isFileSpout()) {
            builder.setSpout("spout", new FileSkylineSpout(topologyConfig.getFileSpoutConfig().getPath()), topologyConfig.getSpouts());
        } else {
            builder.setSpout("spout", new RandomSkylineSpout(topologyConfig.getRandomSpoutConfig(), structure), topologyConfig.getSpouts());
        }

        builder.setBolt("inputSkyline", inputSkylineBolt, parallelism.getInputs())
               .shuffleGrouping("spout")
               .shuffleGrouping("mapSkyline",    "inputSkylineStream")
               .shuffleGrouping("reduceSkyline", "inputSkylineStream")
               .shuffleGrouping("skyline",       "inputSkylineStream");

        builder.setBolt("mapSkyline", mapSkylineBolt, parallelism.getMap())
               .shuffleGrouping("inputSkyline",  "mapSkylineStream");

        builder.setBolt("reduceSkyline", reduceSkylineBolt, parallelism.getReduce())
               .shuffleGrouping("mapSkyline",    "reduceSkylineStream");

        builder.setBolt("skyline", skylineBolt, parallelism.getGlobal())
               .globalGrouping("reduceSkyline",  "skylineStream");

        builder.setBolt("messageSkyline", messageSkylineBolt, parallelism.getMessaging())
               .globalGrouping("skyline",        "messageSkylineStream");
        
        builder.setBolt("storeSkyline", storeSkylineBolt, parallelism.getPersistence())
               .globalGrouping("skyline",        "storeSkylineStream");
        // @formatter:on

        return builder.createTopology();
    }

}
