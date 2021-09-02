package com.zhz_blog.provider_mysql.service;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import com.zhz_blog.common.commonClass.Comment;
import com.zhz_blog.provider_mysql.mapper.ArticlesMapper;
import com.zhz_blog.provider_mysql.mapper.CommentsMapper;
import com.zhz_blog.provider_mysql.mapper.UserAccountMapper;
import com.zhz_blog.provider_mysql.pojo.ArticleComments;
import com.zhz_blog.provider_mysql.pojo.UserAccount;
import com.zhz_blog.provider_mysql.pojo.UserArticles;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DubboService
@Service
public class MySQLService implements MySQLServer {
    @Autowired
    UserAccountMapper userAccountMapper;
    @Autowired
    ArticlesMapper articlesMapper;
    @Autowired
    CommentsMapper commentsMapper;
    @DubboReference(check = false)
    MyRedisServer myRedisServer;

    @Override
    public boolean ifPasswordCorrect(String account, String password) {
        return password.equals(userAccountMapper.getPassword(account));
    }

    @Override
    public boolean ifAccountExist(String account) {
        return userAccountMapper.selectUserIDByAccount(account) != null;
    }

    @Override
    public void createNewUserAccount(String account, String password) {
        userAccountMapper.insert(new UserAccount(account, password));
    }

    @Override
    @Transactional
    public void deleteAccount(String account) {
        //删除账号相关点赞消息
        myRedisServer.deleteLikeByAccount(userAccountMapper.selectUserIDByAccount(account).toString());
        //删除账号信息
        userAccountMapper.deleteAccount(account);
        //删除账户相关文章
        articlesMapper.deleteByAccount(account);
        //删除相关文章的评论
        commentsMapper.deleteByAccount(account);
    }

    @Override
    @Transactional//添加事务
    public void updateUserAccount(String account, String newAccount, String newPassword) {
        //先改密码再改账号，不然找不到账号
        if (newPassword != null) {
            userAccountMapper.updateUserPassword(account, newPassword);
        }
        if (newAccount != null) {
            //删除账号信息
            userAccountMapper.updateUserAccount(account, newAccount);
            //修改相关文章内容
            articlesMapper.updateArticleOwner(account, newAccount);
            //修改文章评论相关内容
            commentsMapper.updateCommentOwner(account, newAccount);
        }
    }

    @Override
    public List<Article> getAllArticlesWhitOutContents(String account) {
        List<Article> result = new ArrayList<>();
        if (account == null) {
            for (UserArticles ua : articlesMapper.getAllArticlesWhitOutContents()) {
                result.add(new Article(ua.getArticle_id(), ua.getArticle_name(),
                        null, ua.getArticle_owner()));
            }
        } else {
            for (UserArticles ua : articlesMapper.getMyArticlesWhitOutContents(account)) {
                result.add(new Article(ua.getArticle_id(), ua.getArticle_name(),
                        null, account));
            }
        }
        return result;
    }

    @Override
    public Article getArticle(Integer articleId) {
        //先从Redis中读取
        Article article = myRedisServer.getArticle(articleId);
        if (article != null) {
            return article;
        }
        UserArticles userArticles = articlesMapper.selectByPrimaryKey(articleId);
        if (userArticles == null) return null;
        article = new Article(articleId, userArticles.getArticle_name(),
                userArticles.getArticle_content(), userArticles.getArticle_owner());
        //Redis中如果没有则存进Redis
        myRedisServer.addArticle(article);
        return article;
    }

    @Override
    public void createNewArticle(String articleOwner, String articleName, String articleContent) {
        articlesMapper.insert(new UserArticles(articleName, articleContent, articleOwner));
    }

    @Override
    public boolean ifArticleTitleRepetitive(String userAccount, String articleTitle, Integer articleId) {
        Integer articleIdByOwnerAndTitle = articlesMapper.getArticleIdByOwnerAndTitle(userAccount, articleTitle);
        if (articleIdByOwnerAndTitle == null) return false;
        return !articleId.equals(articleIdByOwnerAndTitle);
    }

    @Override
    public void editArticle(Integer articleId, String newArticleName, String newContent) {
        articlesMapper.updateArticleByArticleId(new UserArticles(articleId, newArticleName, newContent, null));
        //先删改数据库再删Redis。如果先删Redis，很可能又会读到未修改的文章内容到Redis中。
        myRedisServer.delArticle(articleId);
    }

    @Override
    @Transactional
    public void deleteArticle(Integer articleId) {
        //删除点赞相关信息
        myRedisServer.deleteLikeByArticleId(articleId);
        //删除文章
        articlesMapper.deleteByPrimaryKey(articleId);
        //删除文章相关评论
        commentsMapper.deleteByArticleId(articleId);
        //删除Redis缓存的文章
        myRedisServer.delArticle(articleId);
    }

    @Override
    public List<Integer> getHisAllArticleIDs(Integer userID) {
        return articlesMapper.getAllAIDs(userID);
    }

    @Override
    @Transactional
    public void createNewArticleComment(String userAccount, Integer articleId, String articleComments) {
        if (articlesMapper.getArticleNameById(articleId) != null) {
            //判断文章是否还存在
            commentsMapper.insert(new ArticleComments(articleId, userAccount, articleComments));
        }
    }

    @Override
    public void deleteArticleComment(Integer commentID) {
        commentsMapper.deleteByPrimaryKey(commentID);
    }

    @Override
    public void editArticleComment(Integer commentID, String newArticleCommentContents) {
        commentsMapper.updateCommentContents(new ArticleComments(commentID, null, null, newArticleCommentContents));
    }

    @Override
    public List<Comment> getArticleComments(Integer articleID) {
        List<Comment> result = new ArrayList<>();
        List<ArticleComments> articleComments = commentsMapper.selectByArticleID(articleID);
        for (ArticleComments ac : articleComments) {
            result.add(new Comment(ac.getComment_id(), ac.getComment_article_id()
                    , ac.getComment_owner(), ac.getComment_contents()));
        }
        return result;
    }

    @Override
    public Integer getUserIDByAccount(String account) {
        return userAccountMapper.selectUserIDByAccount(account);
    }
}
