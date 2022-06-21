package io.square.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * @author by 11's papa on 2022年06月13日
 * @version 1.0.0
 */
@Slf4j
public class JacksonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许出现单引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 允许对象忽略json中不存在的属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Ignore null values when writing json.
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Write times as a String instead of a Long so its human readable.
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * object 转其他类型
     *
     * @param object       Object
     * @param valueTypeRef {@code new TypeReference<?>}
     * @return T
     */
    public static <T> T convertValue(Object object, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.convertValue(object, valueTypeRef);
        } catch (Exception e) {
            log.error("反序列化失败", e);
            throw new RuntimeException(e);
        }
    }

    public static String convertToString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("反序列化失败", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * json 字符串转json对象
     *
     * @param jsonString json 字符串转
     * @return com.fasterxml.jackson.databind.JsonNode
     */
    public static JsonNode stringToJsonObject(String jsonString) {
        try {
            return OBJECT_MAPPER.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
