package com.zhz_blog.client.controller.ariticle;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LikeArticleController {
    @DubboReference
    private MyRedisServer myRedisServer;
    @DubboReference
    private MySQLServer mySQLServer;

    @RequestMapping("/zhz/likeArticle")
    public String likeArticle(HttpServletRequest request, Model model, HttpSession session) {
        String aid = request.getParameter("articleId");
        if (aid == null) return "redirect:/zhz/toUserMain";
        int articleId = Integer.parseInt(aid);
        String userAccount = (String) session.getAttribute("userAccount");
        Integer userID = mySQLServer.getUserIDByAccount(userAccount);
        //点赞或取消
        myRedisServer.likeArticle(userID, articleId);
        session.setAttribute("tempArticleId", String.valueOf(articleId));
        return "redirect:/zhz/toArticle";
    }
}
