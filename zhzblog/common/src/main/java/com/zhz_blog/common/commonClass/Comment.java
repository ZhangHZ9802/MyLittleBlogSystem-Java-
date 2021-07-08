package com.zhz_blog.common.commonClass;

import java.io.Serializable;

public class Comment implements Serializable {
    private Integer comment_id;
    private Integer comment_article_id;
    private String comment_owner;
    private String comment_contents;

    public Comment(Integer comment_id, Integer comment_article_id, String comment_owner, String comment_contents) {
        this.comment_id = comment_id;
        this.comment_article_id = comment_article_id;
        this.comment_owner = comment_owner;
        this.comment_contents = comment_contents;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getComment_article_id() {
        return comment_article_id;
    }

    public void setComment_article_id(Integer comment_article_id) {
        this.comment_article_id = comment_article_id;
    }

    public String getComment_owner() {
        return comment_owner;
    }

    public void setComment_owner(String comment_owner) {
        this.comment_owner = comment_owner;
    }

    public String getComment_contents() {
        return comment_contents;
    }

    public void setComment_contents(String comment_contents) {
        this.comment_contents = comment_contents;
    }
}
