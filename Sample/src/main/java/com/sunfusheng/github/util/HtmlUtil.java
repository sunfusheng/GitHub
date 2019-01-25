package com.sunfusheng.github.util;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author by sunfusheng on 2019/1/25
 */
public class HtmlUtil {

    public static String getContributionsData(String htmlText) {
        if (TextUtils.isEmpty(htmlText)) {
            return null;
        }

        Document doc = Jsoup.parse(htmlText);
        Elements elements = doc.getElementsByTag("svg");
        for (Element element : elements) {
            if (element != null) {
                return element.toString();
            }
        }
        return null;
    }

}
