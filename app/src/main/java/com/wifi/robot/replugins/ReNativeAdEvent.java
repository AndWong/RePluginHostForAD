package com.wifi.robot.replugins;

/**
 * Created by wong on 17-8-22.
 */

public class ReNativeAdEvent {
    public int poi;
    public Object object;
    public boolean isSucc;

    public ReNativeAdEvent(int poi, Object object, boolean isSucc) {
        this.poi = poi;
        this.object = object;
        this.isSucc = isSucc;
    }

    public ReNativeAdEvent(boolean isSucc) {
        this.isSucc = isSucc;
    }
}
