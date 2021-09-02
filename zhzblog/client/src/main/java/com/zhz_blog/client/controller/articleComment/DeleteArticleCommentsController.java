package com.zhz_blog.client.controller.articleComment;

import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DeleteArticleCommentsController {
    @DubboReference
    private MySQLServer mySQLServer;

    @RequestMapping("/zhz/deleteArticleComments")
    public String deleteArticleComments(HttpSession session, HttpServletRequest request) {
        String commentID = request.getParameter("commentID");
        if (commentID == null) {
            //非法访问
            return "redirect:/zhz/toUserMain";
        }
        String articleID = request.getParameter("articleID");
        mySQLServer.deleteArticleComment(Integer.valueOf(commentID));
        session.setAttribute("tempArticleId", articleID);
        return "redirect:/zhz/toArticle";
    }
}
