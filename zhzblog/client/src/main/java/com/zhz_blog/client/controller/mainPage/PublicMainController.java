package com.zhz_blog.client.controller.mainPage;

import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PublicMainController {
    @DubboReference
    private MySQLServer mySQLServer;
    @RequestMapping("/zhz/toPublicMain")
    public String toPublicMain(Model model){
        List<Article> allArticles = mySQLServer.getAllArticlesWhitOutContents(null);
        model.addAttribute("articles",allArticles);
        return "/mainPage/publicMainPage";
    }
}
