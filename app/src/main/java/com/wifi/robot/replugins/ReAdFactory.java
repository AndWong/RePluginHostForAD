package com.wifi.robot.replugins;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wong on 17-8-22.
 */

public class ReAdFactory {
    private static Map<Integer,Object> nativeAdMap = new HashMap<>();

    public static void putNativeAD(int key, Object object) {
        nativeAdMap.put(key, object);
    }

    public static Object getNativeAD(int key) {
        if (nativeAdMap.containsKey(key)) {
            return nativeAdMap.get(key);
        } else {
            return null;
        }
    }
}
