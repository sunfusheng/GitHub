package com.sunfusheng.github.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sunfusheng.github.model.Event;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Event> events);

    @Query("select * from Event where login=:username order by created_at desc limit :count")
    List<Event> query(String username, int count);
}
