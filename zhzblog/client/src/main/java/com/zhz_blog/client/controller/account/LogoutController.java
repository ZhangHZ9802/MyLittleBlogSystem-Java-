package com.zhz_blog.client.controller.account;

import com.zhz_blog.api.service.MyRedisServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    @DubboReference
    private MyRedisServer myRedisServer;

    @RequestMapping("/zhz/logout")
    public String logout(HttpSession session) {
        //删除Redis注册，返回欢迎页面
        myRedisServer.removeAccount((String) session.getAttribute("userAccount"));
        //删除session记录
        session.removeAttribute("userAccount");
        return "redirect:/login";
    }
}
