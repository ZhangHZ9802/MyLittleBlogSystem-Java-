package com.zhz_blog.provider_mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserArticles {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer article_id;
    private String article_name;
    private String article_content;
    private String article_owner;

    public UserArticles(String name, String content, String owner){
        article_name = name;
        article_content = content;
        article_owner = owner;
    }
}
