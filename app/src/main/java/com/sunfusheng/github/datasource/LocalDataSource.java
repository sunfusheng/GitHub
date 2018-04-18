package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.ResponseResult;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class LocalDataSource {

    public <T> void emitResult(ObservableEmitter<ResponseResult<T>> emitter, T t) {
        if (t != null) {
            emitter.onNext(ResponseResult.success(t));
        }
        // 控制是否查询完数据库再请求网络
        emitter.onComplete();
    }

    public <T> ObservableTransformer<T, ResponseResult<T>> applyLocalTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseResult.empty();
                    }
                    return ResponseResult.success(it);
                });
    }
}
