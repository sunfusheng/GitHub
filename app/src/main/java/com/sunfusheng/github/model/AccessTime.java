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
    public long lastAccessTime;

    public AccessTime(@NonNull String url, long lastAccessTime) {
        this.url = url;
        this.lastAccessTime = lastAccessTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "AccessTime{" +
                "url='" + url + '\'' +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }
}