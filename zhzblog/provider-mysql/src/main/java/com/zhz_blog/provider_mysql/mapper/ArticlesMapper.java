package com.zhz_blog.provider_mysql.mapper;

import com.zhz_blog.provider_mysql.pojo.UserArticles;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Component
public interface ArticlesMapper extends Mapper<UserArticles> {
    List<UserArticles> getMyArticlesWhitOutContents(String account);

    List<UserArticles> getAllArticlesWhitOutContents();

    void deleteByAccount(String account);

    void updateArticleOwner(String account, String newAccount);

    Integer getArticleIdByOwnerAndTitle(String account, String articleName);

    void updateArticleByArticleId(UserArticles userArticles);

    String getArticleNameById(Integer articleId);

    List<Integer> getAllAIDs(Integer userID);
}
