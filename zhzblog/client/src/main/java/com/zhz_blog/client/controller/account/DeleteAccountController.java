package com.zhz_blog.client.controller.account;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class DeleteAccountController {
    @DubboReference
    private MySQLServer mySQLServer;
    @DubboReference
    private MyRedisServer myRedisServer;

    @RequestMapping("/zhz/deleteAccount")
    public String deleteAccount(HttpSession session) {
        String userAccount = (String) session.getAttribute("userAccount");
        //删除账号
        mySQLServer.deleteAccount(userAccount);
        //删除登录记录
        myRedisServer.removeAccount(userAccount);
        //删除session记录
        session.removeAttribute("userAccount");
        //返回登录界面，要redirect不能直接会页面，不然刷新页面会出现问题
        return "redirect:/login";
    }
}
