package com.plugin.ad.models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告配置操作类
 * Created by wong on 17-6-15.
 */
public class ModelManager {
    public static Map<Integer, NativeConfig> nativeConfigMap = new HashMap<>(); //原生广告位id
    public static Map<String, String> keysMap = new HashMap<>(); //广告的应用key

    private static void putNativeConfig(int poi, NativeConfig config) {
        nativeConfigMap.put(poi, config);
    }

    public static NativeConfig getNativeConfig(int poi) {
        if (nativeConfigMap.containsKey(poi)) {
            return nativeConfigMap.get(poi);
        }
        return null;
    }

    public static void putAdKey(String name, String key) {
        keysMap.put(name, key);
    }

    public static String getAdKey(String name) {
        if (keysMap.containsKey(name)) {
            return keysMap.get(name);
        }
        return null;
    }

    public static void operationNativeConfig(List<NativeConfig> configList) {
        if (null != configList && !configList.isEmpty()) {
            for (NativeConfig config : configList) {
                putNativeConfig(config.poi, config);
            }
        }
    }
}
