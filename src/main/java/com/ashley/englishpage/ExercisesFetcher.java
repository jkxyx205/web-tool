package com.ashley.englishpage;

import com.ashley.core.UrlFetcher;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rick
 * @createdAt 2021-02-23 20:00:00
 */
public class ExercisesFetcher {

    private String url;

    public ExercisesFetcher(String url) {
        this.url  = url;
    }

    public List<String> exercisesUrls() throws IOException {
        final String contextPath = getContextPath();
        return UrlFetcher.fetch(this.url, document -> {
            Elements links = document.select(".intro-ex-menu td:nth-child(1) a");
            return links.stream().map(link-> contextPath + "/" + link.attr("href")).collect(Collectors.toList());
        });
    }

    public List<String> exercisesUrls2() throws IOException {
        final String contextPath = getContextPath();
        return UrlFetcher.fetch(this.url, document -> {
            Elements links = document.select(".intro-ul a");
            return links.stream().filter(link -> link.text().indexOf("Exercise") > -1).map(link-> contextPath + "/" + link.attr("href")).collect(Collectors.toList());
        });
    }

    public String getContextPath() {
        return StringUtils.substringBeforeLast(url, "/");
    }
}
