package com.sunfusheng.github.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private RecyclerViewWrapper recyclerViewWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeTendingData();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        if (getView() == null) return;
        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Tending");
    }

    private void initView() {
        View view = getView();
        if (view == null) return;
        recyclerViewWrapper = view.findViewById(R.id.recyclerViewWrapper);

//        recyclerViewWrapper.setErrorLayout(R.layout.layout_error_default);
//        recyclerViewWrapper.setLoadingState(LoadingState.ERROR);

        recyclerViewWrapper.enableRefresh(false);
        recyclerViewWrapper.enableLoadMore(false);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(true);
        recyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @SuppressLint("CheckResult")
    private void observeTendingData() {
        Api.getWebPageService().fetchTendingRepos("Daily")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    parseTrendingPageData(it.string());
                }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void parseTrendingPageData(String page) {
        Observable.just(page)
                .map(s -> {
                    ArrayList<Repo> repos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, Constants.BASE_WEB_PAGE_URL);
                        Elements elements = doc.getElementsByClass("col-12 d-block width-full py-4 border-bottom");
                        if (elements.size() != 0) {
                            for (Element element : elements) {
                                try {
                                    repos.add(parseTrendingRepositoryData(element));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        repos = null;
                    }

                    return repos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    recyclerViewWrapper.setItems(results);
                });
    }

    private Repo parseTrendingRepositoryData(Element element) throws Exception {
        String fullName = element.select("div > h3 > a").attr("href");
        fullName = fullName.substring(1);
        String owner = fullName.substring(0, fullName.lastIndexOf("/"));
        String repoName = fullName.substring(fullName.lastIndexOf("/") + 1);

        Element descElement = element.select("div > p").first();
        StringBuilder desc = new StringBuilder("");
        for (TextNode textNode : descElement.textNodes()) {
            desc.append(textNode.getWholeText());
        }

        Element numElement = element.getElementsByClass("f6 text-gray mt-2").first();
        String language = "";
        Elements languageElements = numElement.select("span > span");
        if (languageElements.size() > 0) {
            language = numElement.select("span > span").get(1).textNodes().get(0).toString().trim();
        }
        String starNumStr = numElement.select("a").get(0).textNodes().get(1).toString()
                .replaceAll(" ", "").replaceAll(",", "");
        String forkNumStr = numElement.select("a").get(1).textNodes().get(1).toString()
                .replaceAll(" ", "").replaceAll(",", "");
        Element periodElement = numElement.getElementsByClass("d-inline-block float-sm-right").first();
        String periodNumStr = "0";
        if (periodElement != null) {
            periodNumStr = periodElement.childNodes().get(2).toString().trim();
            periodNumStr = periodNumStr.substring(0, periodNumStr.indexOf(" "))
                    .replaceAll(",", "");
        }

        Repo repo = new Repo();
        repo.full_name = fullName;
        repo.name = repoName;
        User user = new User();
        user.login = owner;
        repo.owner = user;
        repo.owner_name = owner;

        repo.description = desc.toString().trim().replaceAll("\n", "");
        repo.stargazers_count = Integer.parseInt(starNumStr);
        repo.forks_count = Integer.parseInt(forkNumStr);
        repo.language = language;
        return repo;
    }

}
