package com.skyline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyStructure;
import com.skyline.utility.StructureLoader;
import com.skyline.utility.TupleFactory;

public class SkylineRunner {

    private final static Logger logger = Logger.getLogger(SkylineRunner.class);

    public static void main(String[] args) {
        try {
            long startTime = System.nanoTime();

            eventSkyline("src/main/resources/structures/basic.json");

            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1000000;

            logger.info(duration + " milliseconds");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void basicSkyline(String structurePath) throws IOException {
        SkyStructure<BasicSkyTuple> structure = StructureLoader.load(structurePath);

        BasicSkyline skyline = new BasicSkyline(structure);

        List<BasicSkyTuple> tuples = TupleFactory.generateMultipleRandomCorrelated(structure, 1000000);

        tuples.forEach(tuple -> {
            skyline.in(tuple);
        });

        logger.info("Success");
        logger.info("Skyline: " + skyline);
    }

    public static void eventSkyline(String structurePath) throws IOException {
        SkyStructure<BasicSkyTuple> structure = StructureLoader.load(structurePath);

        List<BasicSkyTuple> tuples = TupleFactory.generateMultipleRandomCorrelated(structure, 100);

        EventSkyline<BasicSkyTuple> skyline = new EventSkyline<BasicSkyTuple>(structure);

        skyline.setListener(new TupleListener<BasicSkyTuple>() {

            @Override
            public void up(BasicSkyTuple tuple) {

            }

            @Override
            public void down(BasicSkyTuple tuple) {

            }

            @Override
            public void expire(BasicSkyTuple tuple) {

            }

            @Override
            public void change() {

            }

        });

        tuples.stream().forEach(tuple -> {
            skyline.in(tuple);
        });

        skyline.up(new BasicSkyTuple());

        logger.info("Success");
        logger.info("Skyline: " + skyline);
    }

    public static void generateTestData(String structurePath, String outputPath) throws IOException {
        SkyStructure<BasicSkyTuple> structure = StructureLoader.load(structurePath);

        List<BasicSkyTuple> tuples = TupleFactory.generateMultipleRandomCorrelated(structure, 10);

        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File(outputPath), tuples);

        logger.info("Success");
        logger.info("Finished");
    }

}
