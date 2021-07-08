package com.zhz_blog.client.controller.articleComment;

import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class WriteArticleCommentsController {
    @DubboReference
    private MySQLServer mySQLServer;
    @RequestMapping("/zhz/writeArticleComment")
    public String writeArticleComment(HttpServletRequest request, HttpSession session){
        String userAccount = (String) session.getAttribute("userAccount");
        String articleComments = request.getParameter("articleComments");
        String articleId = request.getParameter("articleId");
        mySQLServer.createNewArticleComment(userAccount,Integer.valueOf(articleId),articleComments);
        session.setAttribute("tempArticleId",articleId);
        return "redirect:/zhz/toArticle";
    }
}
