package com.sunfusheng.github.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/5/7.
 */
@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {

    public static final String DB_NAME = "events.db";

    private static volatile EventDatabase instance;

    public static EventDatabase instance() {
        if (instance == null) {
            synchronized (EventDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppUtil.getContext(), EventDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract EventDao getEventDao();

}