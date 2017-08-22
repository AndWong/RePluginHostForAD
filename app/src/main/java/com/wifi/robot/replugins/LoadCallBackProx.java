package com.wifi.robot.replugins;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wong on 17-8-22.
 */

public class LoadCallBackProx implements InvocationHandler {
    /**
     * 这里的话直接去获取对象,
     * 把这个接口的字节码对象数组扔进来就可以了
     */
    public static Object getInstance(ClassLoader classLoader, Class<?> interfaces) {
        return Proxy.newProxyInstance(classLoader, new Class[]{interfaces}, new LoadCallBackProx());
    }

    /**
     * @param o
     * @param method  : 具体的方法名称
     * @param objects : 被代理类的回调方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        //判断是什么方法被调用了嘛
        String methodName = method.getName();
        if ("gdtNativeSuccess".equals(methodName)) {
            int poi = (Integer) objects[0];
            Object object = objects[1];
            ReAdFactory.putNativeAD(poi, object);
            EventHelper.post(new ReNativeAdEvent(poi, object, true));
        } else if (methodName.equals("failure")) {
            EventHelper.post(new ReNativeAdEvent(false));
        }
        return null;
    }
}