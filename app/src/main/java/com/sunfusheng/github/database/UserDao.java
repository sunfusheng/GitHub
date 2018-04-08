package com.sunfusheng.github.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
