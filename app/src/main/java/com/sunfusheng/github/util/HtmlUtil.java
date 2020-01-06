package com.sunfusheng.github.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.function.Consumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019/1/25
 */
public class HtmlUtil {

    public static String getContributionsData(String htmlText) {
        if (TextUtils.isEmpty(htmlText)) {
            return null;
        }

        Document doc = Jsoup.parse(htmlText);
        Elements elements = doc.getElementsByClass("js-calendar-graph-svg");
        for (Element element : elements) {
            if (element != null) {
                return element.toString();
            }
        }
        return null;
    }

    @SuppressLint("CheckResult")
    public static void parseTrendingPageByHtmlText(String htmlText, Consumer<List<Repo>> onSuccess, Consumer<Throwable> onError) {
        Observable.defer(() -> Observable.just(htmlText))
                .subscribeOn(Schedulers.io())
                .map(it -> {
                    List<Repo> tendingRepos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(it, Constants.BASE_URL_GITHUB);
                        Elements elements = doc.getElementsByClass("explore-pjax-container container-lg p-responsive pt-6");
                        if (elements != null && !elements.isEmpty() && elements.get(0) != null) {
                            Elements items = elements.get(0).getElementsByClass("Box-row");
                            if (items != null && !items.isEmpty()) {
                                for (Element element : items) {
                                    try {
                                        tendingRepos.add(parseTrendingReposByElement(element));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return tendingRepos;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess::accept, onError::accept);
    }

    private static Repo parseTrendingReposByElement(Element element) throws Exception {
        String fullName = element.select("article > h1 > a").attr("href");
        if (fullName.length() > 1) {
            fullName = fullName.substring(1);
        }
        String owner = fullName.substring(0, fullName.lastIndexOf("/"));
        String repoName = fullName.substring(fullName.lastIndexOf("/") + 1);

        Element descElement = element.getElementsByClass("col-9 text-gray my-1 pr-4").first();
        StringBuilder desc = new StringBuilder("");
        if (descElement != null) {
            for (TextNode textNode : descElement.textNodes()) {
                desc.append(textNode.getWholeText());
            }
        }

        Element repoInfoElement = element.getElementsByClass("f6 text-gray mt-2").first();
        String language = "";
        Elements languageElements = repoInfoElement.select("d-inline-block ml-0 mr-3");
        if (languageElements != null && languageElements.size() > 1 && languageElements.get(1) != null) {
            language = languageElements.get(1).textNodes().get(0).toString().trim();
        }

        String starCount = "0";
        String forkCount = "0";
        Elements starForkElements = repoInfoElement.select("a");
        if (starForkElements != null) {
            if (starForkElements.size() > 0 && starForkElements.get(0) != null) {
                starCount = starForkElements.get(0).textNodes().get(1).toString().replaceAll(" ", "").replaceAll(",", "");
            }
            if (starForkElements.size() > 1 && starForkElements.get(1) != null) {
                forkCount = starForkElements.get(1).textNodes().get(1).toString().replaceAll(" ", "").replaceAll(",", "");
            }
        }

        String tendingDesc = "";
        Element tendingDescElement = repoInfoElement.getElementsByClass("d-inline-block float-sm-right").first();
        if (tendingDescElement != null) {
            tendingDesc = tendingDescElement.childNodes().get(2).toString().trim();
        }

        User user = new User();
        user.name = owner;

        Repo repo = new Repo();
        repo.full_name = fullName;
        repo.owner = user;
        repo.name = repoName;
        repo.language = language;
        repo.description = desc.toString().trim().replaceAll("\n", "");
        repo.stargazers_count = Integer.parseInt(starCount);
        repo.forks_count = Integer.parseInt(forkCount);
        repo.tendingDesc = tendingDesc;
        return repo;
    }

}
