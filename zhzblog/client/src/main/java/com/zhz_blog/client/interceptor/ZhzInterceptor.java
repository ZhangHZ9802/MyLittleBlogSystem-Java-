package com.zhz_blog.client.interceptor;

import com.zhz_blog.api.service.MyRedisServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class ZhzInterceptor implements HandlerInterceptor {
    @DubboReference
    MyRedisServer myRedisServer;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String userAccount = (String) session.getAttribute("userAccount");
        //如果没有登录的话则不能访问/zhz/*的内容
        if (userAccount ==null||!session.getId().equals(myRedisServer.getSessionIdOfAccount(userAccount))){
            response.sendRedirect("/login");
            return false;//不让过滤器继续，不然会先去到请求的controller。
        }
        return true;
    }
}
