package com.plugin.ad.models;

import java.io.Serializable;

/**
 * 原生广告配置
 * Created by wong on 17-6-15.
 */
public class NativeConfig implements Serializable{
    /**
     * 广告位置
     */
    public int poi;
    /**
     * 广告描述
     */
    public String des;
    /**
     * 广点通广告ID
     */
    public String gdt_id;
}
