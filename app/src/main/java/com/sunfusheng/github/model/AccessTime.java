package com.sunfusheng.github.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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