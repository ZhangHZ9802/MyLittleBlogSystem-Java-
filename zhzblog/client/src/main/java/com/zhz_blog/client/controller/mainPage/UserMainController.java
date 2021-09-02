package com.zhz_blog.client.controller.mainPage;

import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserMainController {
    @DubboReference
    private MySQLServer mySQLServer;

    @RequestMapping("/zhz/toUserMain")
    public String toUserMain(HttpSession session, Model model) {
        String userAccount = (String) session.getAttribute("userAccount");
        model.addAttribute("userAccount", userAccount);//显示用户名
        List<Article> articles = mySQLServer.getAllArticlesWhitOutContents(userAccount);
        model.addAttribute("articles", articles);//显示文章
        return "/mainPage/userMainPage";
    }
}
