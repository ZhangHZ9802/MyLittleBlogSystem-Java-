package com.zhz_blog.common.commonClass;

import java.io.Serializable;

public class Article implements Serializable {
    private Integer article_id;
    private String article_name;
    private String article_content;
    private String article_owner;

    public Article(Integer article_id, String article_name, String article_content, String article_owner) {
        this.article_id = article_id;
        this.article_name = article_name;
        this.article_content = article_content;
        this.article_owner = article_owner;
    }

    public Integer getArticle_id() {
        return article_id;
    }

    public String getArticle_name() {
        return article_name;
    }

    public String getArticle_content() {
        return article_content;
    }

    public String getArticle_owner() {
        return article_owner;
    }
}
