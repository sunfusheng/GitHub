package com.sunfusheng.github.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/8.
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public static final String DB_NAME = "users.db";

    private static volatile UserDatabase instance;

    public static UserDatabase instance() {
        if (instance == null) {
            synchronized (UserDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppUtil.getContext(), UserDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract UserDao getUserDao();

}
