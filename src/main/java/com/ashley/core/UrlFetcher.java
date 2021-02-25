package com.ashley.core;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

/**
 * 根据url获取HTML数据
 * @author Rick
 * @createdAt 2021-02-22 18:59:00
 */
@UtilityClass
public class UrlFetcher {

    /**
     * 根据URL获取
     * @param url
     * @return
     */
    public <R> R fetch(String url, Function<Document, R> function) throws IOException {
        Objects.requireNonNull(url, "url can not be null");
        Document doc = Jsoup.connect(url).get();
        return function.apply(doc);
    }
}
