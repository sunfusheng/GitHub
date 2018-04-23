package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.datasource.RemoteDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.ResponseObserver;
import com.sunfusheng.github.net.ResponseResult;
import com.sunfusheng.github.viewbinder.RepoViewBinder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        View view = getView();
        if (view == null) {
            return;
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MultiTypeAdapter();
        recyclerView.setAdapter(adapter);

        adapter.register(Repo.class, new RepoViewBinder());

        Api.getCommonService().fetchRepos("sfsheng0322", "pushed")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RemoteDataSource.applyRemoteTransformer())
                .subscribe(new ResponseObserver<List<Repo>>() {
                    @Override
                    public void onNotify(ResponseResult<List<Repo>> result) {
                        if (result.loadingState == LoadingState.SUCCESS) {
                            adapter.setItems(result.data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
