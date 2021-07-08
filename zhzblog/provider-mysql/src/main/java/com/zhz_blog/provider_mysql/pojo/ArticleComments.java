package com.zhz_blog.provider_mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "article_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleComments {
    @Id
    @KeySql(useGeneratedKeys = true)//自增键回填
    private Integer comment_id;
    private Integer comment_article_id;
    private String comment_owner;
    private String comment_contents;

    public ArticleComments(Integer comment_article_id, String comment_owner, String comment_contents) {
        this.comment_article_id = comment_article_id;
        this.comment_owner = comment_owner;
        this.comment_contents = comment_contents;
    }
}
