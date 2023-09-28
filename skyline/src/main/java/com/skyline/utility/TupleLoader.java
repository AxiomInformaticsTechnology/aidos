package com.skyline.utility;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.skyline.model.BasicSkyTuple;

public class TupleLoader {

    private final static Logger logger = Logger.getLogger(StructureLoader.class);

    public static List<BasicSkyTuple> load(String path) throws IOException {
        return load(Paths.get(path));
    }

    public static List<BasicSkyTuple> load(Path path) throws IOException {
        logger.info("Loading structure: " + path);
        return MapperUtility.read(path.toFile(), new TypeReference<List<BasicSkyTuple>>() {});
    }

}
