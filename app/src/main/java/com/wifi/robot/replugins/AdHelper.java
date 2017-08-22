package com.wifi.robot.replugins;

import android.content.Context;

import com.qihoo360.replugin.RePlugin;

import java.lang.reflect.Method;

/**
 * Created by wong on 17-8-22.
 */

public class AdHelper {

    /**
     * 通过反射请求广告
     *
     * @param config
     */
    public static void requestAd(String config,int poi) {
        try {
            /**
             * 注意这里的classLoader是RePlugin的classLoader,与系统的ClassLoader有区别
             */
            ClassLoader classLoader = RePlugin.fetchClassLoader("adPlugin");
            /**
             * 需要反射的类
             */
            Class<?> methodClass = classLoader.loadClass("com.plugin.ad.managers.LoadManager");
            /**
             * 被代理的接口
             */
            Class<?> callBackClass = classLoader.loadClass("com.plugin.ad.listeners.ILoadListener");
            /**
             * 这个是动态代理
             * callBackClass : 需要被代理的接口的Class
             * proxListener : 返回的是这个被代理接口的实例
             */
            Object proxListener = LoadCallBackProx.getInstance(classLoader, callBackClass);
            /**
             * callBackClass : 被代理的接口的Class
             * proxListener : 被代理接口的实例
             */
            Method load = methodClass.getDeclaredMethod("requestNativeAD", new Class[]{Context.class, String.class, int.class, callBackClass});
            load.invoke(null, RePlugin.fetchContext("adPlugin"), config, poi, proxListener);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
