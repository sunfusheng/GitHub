package com.sunfusheng.github.cache.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sunfusheng.github.model.Repo;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/27.
 */
@Dao
public interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Repo repo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Repo> repos);

    @Query("select * from Repo where owner_name=:owner_name order by pushed_at desc limit :count")
    List<Repo> query(String owner_name, int count);
}
