package com.ashley.englishpage;

import lombok.*;

import java.util.List;

/**
 * 文章模型
 * @author Rick
 * @createdAt 2021-02-22 19:11:00
 */
@Builder
@Value
public class Article {

    private String title;

    private String subtitle;

    @With
    private String description;

    @With
    private List<String> items;
}
