package com.sunfusheng.github.cache.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/27.
 */
@Database(entities = {Repo.class}, version = 1, exportSchema = false)
public abstract class RepoDatabase extends RoomDatabase {

    private static final String DB_NAME = "repos.db";
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
}