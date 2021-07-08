package com.zhz_blog.provider_redis.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtils {
    public static JedisPool jp = null;
    public static String host = null;
    public static Integer port = null;
    public static Integer MaxTotal = null;
    public static Integer MaxIdle = null;

    static {
        //从properties中取出数据
        ResourceBundle rb = ResourceBundle.getBundle("Jedis");
        host = rb.getString("redis.host");
        port = Integer.parseInt(rb.getString("redis.port"));
        MaxTotal = Integer.parseInt(rb.getString("redis.MaxTotal"));
        MaxIdle = Integer.parseInt(rb.getString("redis.MaxIdle"));
        //新建配置类
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(MaxTotal);//最大链接数
        jpc.setMaxIdle(MaxIdle);//最大活动数
        //新建Jedis池
        jp = new JedisPool(jpc,host,port);
    }
    public static Jedis getJedis(){
        return jp.getResource();
    }
}
