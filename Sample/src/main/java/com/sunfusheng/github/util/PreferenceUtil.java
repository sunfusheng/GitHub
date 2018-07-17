package com.sunfusheng.github.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class PreferenceUtil {

    private static SimpleArrayMap<String, PreferenceUtil> map = new SimpleArrayMap<>();
    private SharedPreferences sp;

    public static PreferenceUtil getInstance() {
        return getInstance("preference_name_default");
    }

    public static PreferenceUtil getInstance(String preferenceName) {
        PreferenceUtil preferenceUtil = map.get(preferenceName);
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(preferenceName);
            map.put(preferenceName, preferenceUtil);
        }
        return preferenceUtil;
    }

    private PreferenceUtil(final String spName) {
        sp = AppUtil.getApp().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public void put(@NonNull final String key, @NonNull final String value) {
        sp.edit().putString(key, value).apply();
    }

    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void put(@NonNull final String key, final int value) {
        sp.edit().putInt(key, value).apply();
    }

    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void put(@NonNull final String key, final long value) {
        sp.edit().putLong(key, value).apply();
    }

    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void put(@NonNull final String key, final float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void put(@NonNull final String key, final boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public void put(@NonNull final String key, @NonNull final Set<String> values) {
        sp.edit().putStringSet(key, values).apply();
    }

    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    public Set<String> getStringSet(@NonNull final String key, @NonNull final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    public void remove(@NonNull final String key) {
        sp.edit().remove(key).apply();
    }

    public void clear() {
        sp.edit().clear().apply();
    }

}
