package com.plugin.ad.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wong on 17-6-15.
 */
public class ADModel implements Serializable{
    public String baidu_key;
    public String gdt_key;
    public List<NativeConfig> place_ids;
}
