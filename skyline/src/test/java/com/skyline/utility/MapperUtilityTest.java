package com.skyline.utility;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyStructure;

public class MapperUtilityTest {

    @Test
    public void testCreateDefault() {
        MapperUtility mapperUtility = new MapperUtility();
        Assert.assertNotNull(mapperUtility);
    }

    @Test
    public void testSerializeSkyTuple() {
        BasicSkyTuple tuple = new BasicSkyTuple();
        String json = MapperUtility.serialize(tuple);
        Assert.assertNotNull(json);
    }

    @Test
    public void testRead() throws JsonParseException, JsonMappingException, IOException {
        SkyStructure<BasicSkyTuple> structure = MapperUtility.read(Paths.get("src/test/resources/structures/cars.json").toFile(), new TypeReference<SkyStructure<BasicSkyTuple>>() {});
        Assert.assertNotNull(structure);
    }

}
