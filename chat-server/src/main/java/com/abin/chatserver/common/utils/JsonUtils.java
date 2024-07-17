package com.abin.chatserver.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T toObj(String str, Class<T> tClass) {
        try {
            return jsonMapper.readValue(str, tClass);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String toStr(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
