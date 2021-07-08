package com.zhz_blog.client.controller.account;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @DubboReference
    private MySQLServer mySQLServer;
    @DubboReference
    private MyRedisServer myRedisServer;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        //在退出页面不退出浏览器的时候可以不登陆直接进
        if(session.getId().equals(myRedisServer.getSessionIdOfAccount((String)session.getAttribute("userAccount")))){
            //进入主界面
            return "redirect:/zhz/toUserMain";
        }
        String userAccount = request.getParameter("userAccount");
        String userPassword = request.getParameter("userPassword");
        if(userAccount==null)return "/account/login";
        //判断密码是否正确
        if (!mySQLServer.ifPasswordCorrect(userAccount, userPassword)) {
            model.addAttribute("callback", "输入账号或密码错误");
            return "/account/login";
        }
        //挤掉其它登陆者
        myRedisServer.registerAccount(userAccount,session.getId());
        //将用户名存入session
        session.setAttribute("userAccount", userAccount);
        //进入主界面
        return "redirect:/zhz/toUserMain";
    }
}
