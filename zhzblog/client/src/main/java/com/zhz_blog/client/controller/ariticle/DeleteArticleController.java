package com.zhz_blog.client.controller.ariticle;

import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DeleteArticleController {
    @DubboReference
    private MySQLServer mySQLServer;
    @RequestMapping("/zhz/deleteArticle")
    public String deleteArticle(HttpServletRequest request){
        mySQLServer.deleteArticle(Integer.valueOf(request.getParameter("articleId")));
        return "redirect:/zhz/toUserMain";
    }
}
