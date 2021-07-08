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
public class RegisterController {
    @DubboReference
    private MySQLServer mySQLServer;
    @DubboReference
    private MyRedisServer myRedisServer;
    @RequestMapping("/register")
    public String registerControl(HttpServletRequest request, Model model, HttpSession session) {
        String userAccount = request.getParameter("userAccount");
        //如果账号为空，则是要前往注册，转向注册界面
        if(userAccount==null) return "/account/register";
        String password1 = request.getParameter("userPassword");
        String password2 = request.getParameter("userPassword2");
        //判断账号是否重复
        if(mySQLServer.ifAccountExist(userAccount)){
            model.addAttribute("callback", "账号已存在");
            return "/account/register";
        }
        //判断两次密码是否一致
        if(!password1.equals(password2)){
            model.addAttribute("callback", "两次的密码不一致");
            return "/account/register";
        }
        //将用户信息写入数据库
        mySQLServer.createNewUserAccount(userAccount, password1);
        myRedisServer.registerAccount(userAccount,session.getId());
        //将账号信息写入session
        session.setAttribute("userAccount", userAccount);
        //进入主页面
        return "redirect:/zhz/toUserMain";
    }
}
