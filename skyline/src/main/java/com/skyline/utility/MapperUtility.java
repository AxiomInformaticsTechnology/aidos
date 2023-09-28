package com.skyline.utility;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtility {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serialize(Object obj) {
        String json = obj.getClass().getName();
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "\n" + json;
    }

    public static <T> T read(File file, TypeReference<T> reference) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(file, reference);
    }

}
