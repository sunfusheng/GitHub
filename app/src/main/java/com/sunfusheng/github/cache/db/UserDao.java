package com.sunfusheng.github.cache.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sunfusheng.github.model.User;

/**
 * @author sunfusheng on 2018/4/8.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("select * from User where login like :login limit 1")
    User query(String login);

}
