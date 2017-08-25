package com.plugin.ad.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * json工具类
 * Created by wong on 17-6-15.
 */
public class JsonUtil {
    private static Gson gson = null;
    private static JsonUtil jsonUtil = null;

    private JsonUtil() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public static JsonUtil getInstance() {
        if(jsonUtil == null) {
            jsonUtil = new JsonUtil();
        }
        return jsonUtil;
    }

    /**
     * 将对象转换为JSON字符串
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 将JSON字符串转化为对象
     * @param s
     * @param cls
     * @return
     */
    public <T>T fromJson(String s, Type cls) {
        return gson.fromJson(s.trim(), cls);
    }
}
