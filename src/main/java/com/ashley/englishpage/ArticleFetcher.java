package com.ashley.englishpage;

import com.ashley.core.UrlFetcher;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rick
 * @createdAt 2021-02-22 19:15:00
 */
public class ArticleFetcher {

    public Article[] fetchAll(String url) throws IOException {
        System.out.println(url);
        Article blank = fetchBlank(url);
        Article answer = fetchAnswer(url, blank);
        return new Article[] {blank, answer};
    }

    /**
     * 根据URL获取
     * @param url
     * @return
     */
    private Article fetchBlank(String url) throws IOException {
       return UrlFetcher.fetch(url, document -> {
           Element titleElement = document.selectFirst(".titleTop");
           Element subTitleElement = document.selectFirst(".titleBottom");

           String title = null;
           if (Objects.isNull(titleElement)) {
               titleElement = document.selectFirst(".Titles h1");
           }
           if (Objects.nonNull(titleElement)) {
               title = titleElement.text();
           }

           String subTitle = null;
           if (Objects.nonNull(subTitleElement)) {
               subTitle = subTitleElement.text();
           }

           String description = null;
           Element descriptionElement = document.selectFirst("#Instructions");
           if (Objects.nonNull(descriptionElement)) {
               description = descriptionElement.text();
           }

           Element contentElement = document.selectFirst(".ClozeBody");
           List<String> items;
           if (Objects.nonNull(contentElement)) {
               items = handleBlankBody(contentElement.html());
           } else {
               items = Collections.emptyList();
           }
           return Article.builder().title(title).subtitle(subTitle).description(description).items(items).build();
        });
    }

    private Article fetchAnswer(String url, Article blank) throws IOException {
        String command = "/Users/rick/dev/phantomjs-2.1.1-macosx/bin/phantomjs /Users/rick/Documents/phantomjs-2.1.1-macosx/examples/atest.js " + url;

        Process proc = Runtime.getRuntime().exec(command);
        String itemHTML = IOUtils.toString(proc.getInputStream(), "utf-8");

        if (Objects.isNull(blank)) {
            blank = fetchBlank(url);
        }

        return blank.withItems(handleAnswerBody(itemHTML));
    }

    private List<String> handleBlankBody(String html)  {
        html = html.replaceAll("<span class=\"GapSpan\".+?<\\/span>", "_______").replaceAll("<.+?>","");
        return Arrays.stream(html.split("\\n+")).collect(Collectors.toList());
    }

    private List<String> handleAnswerBody(String html)  {
        html = html.replaceAll("<span class=\"GapSpan\" id=\"GapSpan\\d+\">(.+?)<\\/span>", "*$1*")
                .replaceAll("<br>", "##")
                .replaceAll("<.+?>","")
                .replaceAll("\\n+", "");
        return Arrays.stream(html.split("(##)+")).collect(Collectors.toList());
    }
}
