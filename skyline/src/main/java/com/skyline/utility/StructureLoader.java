package com.skyline.utility;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.skyline.model.SkyStructure;
import com.skyline.model.SkyTuple;

public class StructureLoader {

    private final static Logger logger = Logger.getLogger(StructureLoader.class);

    public static <T extends SkyTuple> SkyStructure<T> load(String path) throws IOException {
        return load(Paths.get(path));
    }

    public static <T extends SkyTuple> SkyStructure<T> load(Path path) throws IOException {
        logger.info("Loading structure: " + path);
        return MapperUtility.read(path.toFile(), new TypeReference<SkyStructure<T>>() {});
    }

}
