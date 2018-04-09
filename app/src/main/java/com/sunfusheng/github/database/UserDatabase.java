package com.sunfusheng.github.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sunfusheng.github.model.User;

/**
 * @author sunfusheng on 2018/4/8.
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DB_NAME = "user.db";

    public static UserDatabase getDefault(Context context) {
        return buildDatabase(context);
    }

    private static UserDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public abstract UserDao getUserDao();

}
