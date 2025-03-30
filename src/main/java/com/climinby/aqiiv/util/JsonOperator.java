package com.climinby.aqiiv.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonOperator {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> Map<String, T> readMap(InputStream jsonStream, Class<T> valueType) throws IOException {
        return OBJECT_MAPPER.readValue(jsonStream, Map.class);
    }

    public static <T> T readObject(InputStream jsonStream, Class<T> objectType) throws IOException {
        return OBJECT_MAPPER.readValue(jsonStream, objectType);
    }

    public static <T> T readObject(File jsonFile, Class<T> objectType) throws IOException {
        return OBJECT_MAPPER.readValue(jsonFile, objectType);
    }

    public static <T> T readObject(File jsonFile, TypeReference<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(jsonFile, type);
    }

    public static void writeObject(File jsonFile, Object obj) throws IOException {
        OBJECT_MAPPER.writeValue(jsonFile, obj);
    }
}
