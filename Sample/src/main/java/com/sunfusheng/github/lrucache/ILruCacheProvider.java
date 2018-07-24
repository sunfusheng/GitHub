package com.sunfusheng.github.lrucache;

/**
 * @author sunfusheng on 2018/7/24.
 */
public interface ILruCacheProvider<K, V> {
    V put(K key, V value);

    V get(K key);

    V remove(K key);

    void clear();
}
