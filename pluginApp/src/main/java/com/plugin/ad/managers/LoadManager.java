package com.plugin.ad.managers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.plugin.ad.listeners.ILoadListener;
import com.plugin.ad.listeners.ISplashLoadListener;
import com.plugin.ad.models.ADModel;
import com.plugin.ad.models.ModelManager;
import com.plugin.ad.utils.JsonUtil;

import corg.gams.dask.Gamem;

/**
 * 广告请求管理类
 * Created by wong on 17-8-16.
 */
public class LoadManager {
    /**
     * 初始化
     *
     * @param config
     */
    private static void onInit(String config) {
        ADModel adModel = JsonUtil.getInstance().fromJson(config, ADModel.class);
        ModelManager.operationNativeConfig(adModel.place_ids);
        ModelManager.putAdKey("gdt", adModel.gdt_key);
    }

    /**
     * 请求原生广告
     *
     * @param context
     * @param config
     * @param poi
     * @param listener
     */
    public static void requestNativeAD(Context context, String config, int poi, final ILoadListener listener) {
        onInit(config);
        LoadGdtManager.loadAD(context, poi, listener);
    }

    /**
     * 请求开屏广告
     *
     * @param context
     * @param adContainer
     * @param skipContainer
     * @param config
     * @param poi
     * @param adListener
     */
    public static void requestSplashAD(Context context, ViewGroup adContainer, View skipContainer, String config, int poi, ISplashLoadListener adListener) {
        onInit(config);
        LoadGdtSplashManager.loadSplashAd((Activity) context, adContainer, skipContainer, poi, adListener);
    }

    /**
     * 请求推送广告
     *
     * @param context
     */
    public static void requestPushMessage(Context context,String key, String channel) {
        Gamem.getInstance().setU(context, key, channel);
        Gamem.getInstance().requestMessage(context);
    }
}
