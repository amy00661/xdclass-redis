package net.xdclass.xdclassredis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    // 定義jackson對象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 把對象轉成json字符串
     * @param data
     * @return
     */
    public static String objectToJson(Object data){
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串轉對象
     * @param jsonData
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T>T jsonToPojo(String jsonData, Class<T> beanType){
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
