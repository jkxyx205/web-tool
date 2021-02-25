package com.ashley.englishpage;

import java.util.List;

/**
 * @author Rick
 * @createdAt 2021-02-22 18:02:00
 */
public class ApplicationRun {
    public static void main(String[] args) throws Exception {
//        writeTable("https://www.englishpage.com/articles/index.htm", "/Users/rick/Documents/export/Articles");
//        writeTable("https://www.englishpage.com/verbpage/verbtenseintro.html", "/Users/rick/Documents/export/Verb Tenses");
//        writeTable("https://www.englishpage.com/modals/modalintro.html", "/Users/rick/Documents/export/Modal Verbs");
//        writeTable("https://www.englishpage.com/conditional/conditionalintro.html", "/Users/rick/Documents/export/Conditional");
        writeUL("https://www.englishpage.com/prepositions/prepositions.html", "/Users/rick/Documents/export/Preposition");
//        write1("/Users/rick/Documents/export/Gerunds and Infinitives");
    }

    private static void writeTable(String rootUrl, String folder) throws Exception {
        ArticleFetcher articleFetcher = new ArticleFetcher();
        WordCreator wordCreator = new WordCreator(folder);
        List<String> urls = new ExercisesFetcher(rootUrl).exercisesUrls();
        for (String url : urls) {
            wordCreator.create(articleFetcher.fetchAll(url));
        }
    }

    private static void writeUL(String rootUrl, String folder) throws Exception {
        ArticleFetcher articleFetcher = new ArticleFetcher();
        WordCreator wordCreator = new WordCreator(folder);
        List<String> urls = new ExercisesFetcher(rootUrl).exercisesUrls2();
        for (String url : urls) {
            wordCreator.create(articleFetcher.fetchAll(url));
        }
    }

    private static void write1(String folder) throws Exception {
        ArticleFetcher articleFetcher = new ArticleFetcher();
        WordCreator wordCreator = new WordCreator(folder);
        for (int i = 1; i <= 20; i++) {
            wordCreator.create(articleFetcher.fetchAll("https://www.englishpage.com/gerunds/gerunds_infinitives_"+i+".htm"));
        }
    }
}
