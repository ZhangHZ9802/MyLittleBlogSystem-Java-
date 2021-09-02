package com.zhz_blog.provider_redis.service;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import com.zhz_blog.provider_redis.utils.JedisUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;
import java.util.Set;

@DubboService
@Service
public class MyRedisService implements MyRedisServer {
    @DubboReference(check = false)
    MySQLServer mySQLServer;

    private final Jedis jedis = JedisUtils.getJedis();

    @Override
    public void registerAccount(String account, String sessionId) {
        jedis.set(account + ":logged", sessionId);
    }

    @Override
    public void removeAccount(String account) {
        jedis.del(account + ":logged");
    }

    @Override
    public String getSessionIdOfAccount(String account) {
        return jedis.get(account + ":logged");
    }

    @Override
    public void likeArticle(Integer userID, Integer articleId) {
        //使用userID和articleId而不是用account的原因：
        // 1.当账号信息/文章信息修改时，不需要修改redis数据库；
        // 2.当账号/文章删除时，相关数据即使不删除也不会影响使用（节约性能）（但是会占用redis内存，可以在空闲的时候逐个排删已删除的账号或文章信息）
        String aID = articleId.toString();
        String uID = userID.toString();
        Long sadd = jedis.sadd(uID + ":likedArticle", aID);
        jedis.sadd(aID + ":likedBy", uID);
        if (sadd == 1L) {
            //添加成功，没有赞过
            jedis.hsetnx("article:like:counts", aID, "0");
            jedis.hincrBy("article:like:counts", aID, 1L);
        } else {
            //添加失败之前赞过，现在取消
            jedis.srem(uID + ":likedArticle", aID);
            jedis.srem(aID + ":likedBy", uID);
            jedis.hincrBy("article:like:counts", aID, -1L);
        }
    }

    @Override
    public int getArticleLikedCounts(Integer articleID) {
        String aID = articleID.toString();
        String counts = jedis.hget("article:like:counts", aID);
        if (counts == null) {
            return 0;
        } else {
            return Integer.parseInt(counts);
        }
    }

    @Override
    public Article getArticle(Integer articleID) {
        String aID = articleID.toString();
        String name = jedis.hget("hotArticle:" + aID, "article:name");
        if (name == null) {//没有该文章
            return null;
        }
        String content = jedis.hget("hotArticle:" + aID, "article:content");
        String owner = jedis.hget("hotArticle:" + aID, "article:owner");
        return new Article(articleID, name, content, owner);
    }

    @Override
    public void addArticle(Article article) {
        String articleID = article.getArticle_id().toString();
        Random random = new Random();
        //防止文章集中失效
        int randomTime = random.nextInt(30 * 60) + 30 * 60;
        Transaction multi = jedis.multi();
        try {
            multi.hsetnx("hotArticle:" + articleID, "article:name", article.getArticle_name());
            multi.hsetnx("hotArticle:" + articleID, "article:content", article.getArticle_content());
            multi.hsetnx("hotArticle:" + articleID, "article:owner", article.getArticle_owner());
            multi.hsetnx("hotArticle:" + articleID, "article:id", articleID);
            multi.expire("hotArticle:" + articleID, randomTime);
        } catch (Exception e) {
            multi.discard();
            e.printStackTrace();
        }
        multi.exec();
    }

    @Override
    public void delArticle(Integer articleID) {
        jedis.del("hotArticle:" + articleID.toString());
    }

    @Override
    public void deleteLikeByAccount(String uID) {
        //获取该用户喜欢过的文章
        Set<String> likedArticles = jedis.smembers(uID + ":likedArticle");
        //删除该账号所有喜欢的文章记录
        jedis.del(uID + ":likedArticle");
        //所有点赞过的文章的喜欢数减少
        for (String articleID : likedArticles) {
            jedis.hincrBy("article:like:counts", articleID, -1L);
        }
        //获取该用户自己的所有文章id
        List<Integer> aIDs = mySQLServer.getHisAllArticleIDs(Integer.parseInt(uID));
        //依据文章的id删除所有文章的点赞,以及所有点过赞的人
        for(int aID:aIDs){
            deleteLikeByArticleId(aID);
        }
    }


    @Override
    //文章被删除后，需要删除与文章关联的点赞
    public void deleteLikeByArticleId(Integer articleId) {
        String aID = articleId.toString();
        //删除文章相关点赞数
        jedis.hdel("article:like:counts", aID);
        //获取点赞过文章的人的id，删除他们的点赞消息
        Set<String> userIDs = jedis.smembers(aID + ":likedBy");
        for (String uID : userIDs) {
            jedis.srem(uID + ":likedArticle", aID);
        }
        //删除文章被喜欢的人的集合
        jedis.del(aID + ":likedBy");
    }
}
