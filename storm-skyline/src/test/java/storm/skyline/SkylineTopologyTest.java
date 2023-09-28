package storm.skyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.ILocalCluster;
import org.apache.storm.Testing;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.testing.CompleteTopologyParam;
import org.apache.storm.testing.MkClusterParam;
import org.apache.storm.testing.MockedSources;
import org.apache.storm.testing.TestJob;
import org.apache.storm.tuple.Values;
import org.junit.Assert;
import org.junit.Test;

import com.skyline.model.BasicSkyTuple;
import com.skyline.utility.TupleLoader;

import storm.skyline.config.TopologyConfig;
import storm.skyline.model.StormSkyTuple;

public class SkylineTopologyTest {

    @Test
    public void testCorrelatedMax3dSkyline() {
        Config daemonConf = new Config();
        daemonConf.put(Config.STORM_LOCAL_MODE_ZMQ, false);

        MkClusterParam mkClusterParam = new MkClusterParam();
        mkClusterParam.setSupervisors(8);
        mkClusterParam.setDaemonConf(daemonConf);

        Testing.withSimulatedTimeLocalCluster(mkClusterParam, new TestJob() {

            @Override
            public void run(ILocalCluster cluster) throws Exception {

                TopologyConfig topologyConfig = SkylineTopology.loadTopologyConfig("src/test/resources/config/basic-topology.yaml");

                StormTopology topology = SkylineTopology.buildTopology(topologyConfig);

                Assert.assertNotNull(topology);

                MockedSources mockedSources = new MockedSources();

                List<BasicSkyTuple> tuples = TupleLoader.load(topologyConfig.getFileSpoutConfig().getPath());

                List<Values> values = new ArrayList<Values>();

                tuples.forEach(tuple -> {
                    Values value = new Values(new StormSkyTuple(tuple));
                    mockedSources.addMockData("spout", value);
                    values.add(value);
                });

                Config config = SkylineTopology.buildConfig(topologyConfig);

                CompleteTopologyParam completeTopologyParam = new CompleteTopologyParam();
                completeTopologyParam.setMockedSources(mockedSources);
                completeTopologyParam.setStormConf(config);

                @SuppressWarnings("rawtypes")
                Map result = Testing.completeTopology(cluster, topology, completeTopologyParam);

                List<?> skyTuples = Testing.readTuples(result, "spout");
                Assert.assertTrue(Testing.multiseteq(values, skyTuples));

                Assert.assertEquals(5000, skyTuples.size());

            }

        });
    }

}
