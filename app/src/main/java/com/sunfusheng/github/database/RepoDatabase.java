package com.sunfusheng.github.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/27.
 */
@Database(entities = {Repo.class, User.class}, version = 1, exportSchema = false)
public abstract class RepoDatabase extends RoomDatabase {

    public static final String DB_NAME = "repos.db";

    private static volatile RepoDatabase instance;

    public static RepoDatabase instance() {
        if (instance == null) {
            synchronized (RepoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(AppUtil.getContext(), RepoDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract RepoDao getRepoDao();

    public abstract UserDao getUserDao();

}