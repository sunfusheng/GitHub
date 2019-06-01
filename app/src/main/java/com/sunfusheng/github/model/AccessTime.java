package com.sunfusheng.github.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author by sunfusheng on 2019/6/1.
 */
@Entity(indices = {@Index(value = "url", unique = true)})
public class AccessTime {
    @PrimaryKey
    @NonNull
    public String url = "";
    public long accessTime;

    public AccessTime(@NonNull String url, long accessTime) {
        this.url = url;
        this.accessTime = accessTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "AccessTime{" +
                "url='" + url + '\'' +
                ", accessTime=" + accessTime +
                '}';
    }
}