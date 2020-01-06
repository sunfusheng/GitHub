package com.sunfusheng.github.cache.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
