package com.zhz_blog.api.service;

import com.zhz_blog.common.commonClass.Article;
import com.zhz_blog.common.commonClass.Comment;

import java.util.List;

public interface MySQLServer {
    //account
    boolean ifPasswordCorrect(String account, String password);
    boolean ifAccountExist(String account);
    void createNewUserAccount(String account, String password);
    void deleteAccount(String account);
    void updateUserAccount(String account, String newAccount, String newPassword);
    //article
    List<Article> getAllArticlesWhitOutContents(String account);
    Article getArticle(Integer articleId);
    void createNewArticle(String articleOwner, String articleName, String articleContent);
    boolean ifArticleTitleRepetitive(String account,String articleName,Integer articleId);
    void editArticle(Integer articleId, String newArticleName, String newContent);
    void deleteArticle(Integer articleId);
    //comment
    void createNewArticleComment(String userAccount,Integer articleId,String articleComments);
    void deleteArticleComment(Integer commentID);
    void editArticleComment(Integer commentID,String newArticleCommentContents);
    List<Comment> getArticleComments(Integer articleID);
    Integer getUserIDByAccount(String account);
}
