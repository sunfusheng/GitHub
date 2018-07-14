package com.sunfusheng.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author sunfusheng on 2018/7/13.
 */
public class Label {
    public int id;
    public String node_id;
    public String url;
    public String name;
    public String color;
    @SerializedName("default")
    public boolean defaultX;
}
