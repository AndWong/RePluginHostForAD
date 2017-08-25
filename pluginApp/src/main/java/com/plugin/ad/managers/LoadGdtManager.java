package com.plugin.ad.managers;

import android.content.Context;

import com.plugin.ad.listeners.ILoadListener;
import com.plugin.ad.models.ModelManager;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.List;

/**
 * Created by wong on 17-8-16.
 */

public class LoadGdtManager {
    /**
     * 请求广告
     *
     * @param context  必须是该插件的Context
     * @param poi
     * @param listener
     */
    public static void loadAD(Context context, final int poi, final ILoadListener listener) {
        NativeAD nativeAD = new NativeAD(context, getGdtKey(), getGdtId(poi), new NativeAD.NativeAdListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> list) {
                if (null != list && !list.isEmpty()) {
                    listener.gdtNativeSuccess(poi, list.get(0));
                } else {
                    listener.failure("Empty");
                }
            }

            @Override
            public void onNoAD(int i) {
                listener.failure("onNoAD->" + i);
            }

            @Override
            public void onADStatusChanged(NativeADDataRef nativeADDataRef) {
            }

            @Override
            public void onADError(NativeADDataRef nativeADDataRef, int i) {

            }
        });
        nativeAD.loadAD(1);
    }

    private static String getGdtKey() {
        return ModelManager.getAdKey("gdt");
    }

    private static String getGdtId(int poi) {
        return ModelManager.getNativeConfig(poi).gdt_id;
    }
}
