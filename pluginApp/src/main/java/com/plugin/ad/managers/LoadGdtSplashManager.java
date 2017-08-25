package com.plugin.ad.managers;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.plugin.ad.listeners.ISplashLoadListener;
import com.plugin.ad.models.ModelManager;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

/**
 * 广点通开屏广告
 * Created by wong on 17-8-17.
 */
public class LoadGdtSplashManager {

    /**
     * 请求开屏广告
     *
     * @param activity
     * @param adContainer
     * @param skipContainer
     * @param poi
     * @param adListener
     */
    public static void loadSplashAd(Activity activity, ViewGroup adContainer, View skipContainer, int poi, final ISplashLoadListener adListener) {
        new SplashAD(activity, adContainer, skipContainer, getAppid(), getId(poi), new SplashADListener() {
            @Override
            public void onADDismissed() {
                adListener.onADDismissed();
            }

            @Override
            public void onNoAD(int i) {
                adListener.onAdFailed(String.valueOf(i));
            }

            @Override
            public void onADPresent() {
                adListener.onADPresent(true);
            }

            @Override
            public void onADClicked() {
                adListener.onADClicked(true);
            }

            @Override
            public void onADTick(long l) {
                adListener.onADTick(l);
            }
        }, 0);
    }

    private static String getAppid() {
        return ModelManager.getAdKey("gdt");
    }

    private static String getId(int poi) {
        return ModelManager.getNativeConfig(poi).gdt_id;
    }
}
