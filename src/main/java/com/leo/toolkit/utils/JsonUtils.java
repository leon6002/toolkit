package com.leo.toolkit.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        MAPPER.registerModule(module);
    }

    public static JsonNode toJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * MyObject[] myObjects = JsonUtil.readObject(jsonString,MyObject[].class);
     * MyObject myObject = JsonUtil.readObject(jsonString,MyObject.class);
     */
    public static <T> T readObject(String json, Class<T> cls) {
        try {
            return MAPPER.readValue(json, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readObject(JsonNode node, Class<T> cls) {
        try {
            return MAPPER.treeToValue(node, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> readMap(String json) {
        try {
            return MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 性能较差，建议使用 List<MyObject> list = Arrays.asList(JsonUtils.readObject(json, MyObject[].class));
     */
    public static <T> List<T> readList(String json, Class<T> clazz) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
            return MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用json pointer方式读取数组节点
     * JsonUtils.readList(rootNode, "/path1/path2" ,MyObject.class)
     */
    public static <T> List<T> readList(JsonNode root, String path, Class<T> cls) {
        try {
            List<T> list = new ArrayList<>();
            for (JsonNode subNode : root.at(path)) {
                list.add(MAPPER.treeToValue(subNode, cls));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> readList(JsonNode node, Class<T> clazz) {
        try {
            List<T> list = new ArrayList<>();
            for (JsonNode subNode : node) {
                list.add(MAPPER.treeToValue(subNode, clazz));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readObject(JsonNode root, String path, Class<T> clazz) {
        try {
            JsonNode node = root.at(path);
            return node instanceof MissingNode ? null : MAPPER.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
