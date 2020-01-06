package com.sunfusheng.github.cache.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sunfusheng.github.model.AccessTime;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/8.
 */
@Database(entities = {AccessTime.class}, version = 1, exportSchema = false)
public abstract class AccessTimeDatabase extends RoomDatabase {

    private static final String DB_NAME = "access_time.db";
    private static volatile AccessTimeDatabase instance;

    public static AccessTimeDatabase instance() {
        if (instance == null) {
            synchronized (AccessTimeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppUtil.getContext(), AccessTimeDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract AccessTimeDao getAccessTimeDao();
}
