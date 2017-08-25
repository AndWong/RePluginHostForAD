package com.plugin.ad.listeners;

import com.qq.e.ads.nativ.NativeADDataRef;

/**
 * Created by wong on 17-8-16.
 */
public interface ILoadListener {
    /**
     * 加载广点通广告成功
     *
     * @param poi
     * @param nativeADDataRef
     */
    void gdtNativeSuccess(int poi, NativeADDataRef nativeADDataRef);

    /**
     * 加载广告失败
     *
     * @param error
     */
    void failure(String error);
}
