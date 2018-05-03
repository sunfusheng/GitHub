package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.RepoDatabase;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoLocalDataSource extends LocalDataSource implements IRepoDataSource {

    private static RepoLocalDataSource instance = new RepoLocalDataSource();

    private RepoLocalDataSource() {
    }

    public static RepoLocalDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<List<Repo>>> getRepos(String username, int page, int perPage) {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseResult<List<Repo>>>) emitter -> {
            emitResult(emitter, RepoDatabase.instance().getRepoDao().query(username, perPage));
        })).subscribeOn(Schedulers.io());
    }

}
