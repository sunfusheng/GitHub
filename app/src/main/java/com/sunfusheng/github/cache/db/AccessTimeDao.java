package com.sunfusheng.github.cache.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sunfusheng.github.model.AccessTime;

/**
 * @author by sunfusheng on 2019/6/1.
 */
@Dao
public interface AccessTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccessTime accessTime);

    @Query("select * from AccessTime where url like :url limit 1")
    AccessTime query(String url);
}
