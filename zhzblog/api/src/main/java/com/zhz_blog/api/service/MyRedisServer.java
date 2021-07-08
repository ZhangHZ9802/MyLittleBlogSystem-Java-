package com.zhz_blog.api.service;

import com.zhz_blog.common.commonClass.Article;

public interface MyRedisServer {
    //account
    void registerAccount(String account,String sessionID);
    void removeAccount(String account);
    String getSessionIdOfAccount(String account);
    //like
    void likeArticle(Integer userID,Integer articleID);
    int getArticleLikedCounts(Integer articleID);
    //hotArticle
    Article getArticle(Integer articleID);
    void addArticle(Article article);
    void delArticle(Integer articleID);
//    void deleteLikeByAccount(String account);
//    void updateLikeOwner(String account,String newAccount);
//    void deleteLikeByArticleId(Integer articleId);
}
