package com.lee.util.json;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.*;

public class JsonUtil {

    private static final ObjectMapper om = new ObjectMapper();
    private static final ObjectMapper unknown = new ObjectMapper();
    private static final ObjectMapper annotation;

    static {
        unknown.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        unknown.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        unknown.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        annotation = new ObjectMapper();
        annotation.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        annotation.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        annotation.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        annotation.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    public JsonUtil() {
    }

    public static <T> String toJSONignoreAnnotations(T object) {

        try {
            return annotation.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException", e);
        }
    }

    public static <T> String toJSONwithOutNullProp(T object) {

        try {
            return unknown.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException", e);
        }
    }

    public static <T> String toJson(T object) {
        ObjectMapper mapper = getObjectMapper(false);

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException", e);
        }
    }

    public static ObjectMapper getObjectMapper(boolean isIgnoreUnknownProperties) {
        return isIgnoreUnknownProperties ? unknown : om;
    }

    public static <T> T fromJSON(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    public static <T> T fromJSON(String jsonStr, Class<T> clazz, boolean isIgnoreUnknownProperties) {
        ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);
        Object object = null;

        try {
            object = mapper.readValue(jsonStr, clazz);
            return (T) object;
        }catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> T fromJSON(String jsonStr, TypeReference<T> typeReference, boolean isIgnoreUnknownProperties) {
        ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);
        Object object = null;

        try {
            object = mapper.readValue(jsonStr, typeReference);
            return (T) object;
        }catch (IOException var8) {
            throw new RuntimeException("IOException", var8);
        }
    }

    public static <T> List<T> fromJSONToList(String jsonStr, Class<T> clazz) {
        return fromJSONToList(jsonStr, clazz, false);
    }

    private static <T> List<T> fromJSONToList(String jsonStr, Class<T> clazz, boolean isIgnoreUnknownProperties) {
        ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);

        try {
            JsonNode jsonNode = mapper.readTree(jsonStr);
            Iterator<JsonNode> it = jsonNode.elements();

            List<T> list = new ArrayList<>();

            while (it.hasNext()) {
                list.add(fromJSON(it.next().toString(), clazz));
            }

            return list;

        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> Map<String, T> fromJSONToMap(String jsonString, Class<T> clazz) {
        return fromJSONToMap(jsonString, clazz, false);
    }

    private static <T> Map<String, T> fromJSONToMap(String jsonStr, Class<T> clazz, boolean isIgnoreUnknownProperties) {
        ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);

        try{
            JsonNode rootNode = mapper.readTree(jsonStr);
            Iterator<String> it = rootNode.fieldNames();
            LinkedHashMap map = new LinkedHashMap();

            while(it.hasNext()) {
                String nodeName = it.next();
                T t = fromJSON(rootNode.path(nodeName).toString(), clazz);
                map.put(nodeName, t);
            }

            return map;
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

}
