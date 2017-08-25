package com.plugin.ad.listeners;

/**
 * 开屏广告回调监听
 * Created by wong on 17-8-17.
 */
public interface ISplashLoadListener {
    void onADDismissed();

    void onADPresent(boolean isGdt);

    void onADClicked(boolean isGdt);

    void onADTick(long var1);

    void onAdFailed(String var1);
}
