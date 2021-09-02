package com.zhz_blog.client.listener;

import com.zhz_blog.api.service.MyRedisServer;
import org.apache.dubbo.config.annotation.DubboReference;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class AllSessionListener implements HttpSessionListener {
    @DubboReference
    MyRedisServer myRedisServer;

    @Override
    //检测到浏览器退出，将session中登录的信息给删除
    public void sessionDestroyed(HttpSessionEvent se) {
        String userAccount = (String) se.getSession().getAttribute("userAccount");
        //如果未登录或者已经退出，session中将没有userAccount信息，所以不用去Redis中删除（防止误删或出现错误）。
        if (userAccount != null && (!"".equals(userAccount))) {
            myRedisServer.removeAccount(userAccount);
        }
    }
}
