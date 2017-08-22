package com.wifi.robot.replugins;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wong on 17-8-22.
 */

public class EventHelper {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unRegister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }
}
