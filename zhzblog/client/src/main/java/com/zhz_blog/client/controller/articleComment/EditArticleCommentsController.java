package com.zhz_blog.client.controller.articleComment;

import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EditArticleCommentsController {
    @DubboReference
    private MySQLServer mySQLServer;

    @RequestMapping("/zhz/editArticleComment")
    public String editArticleComment(HttpServletRequest request, HttpSession session) {
        String articleID = request.getParameter("articleID");
        if (articleID == null) {
            //非法访问
            return "redirect:/zhz/toUserMain";
        }
        String newArticleCommentContents = request.getParameter("newArticleCommentContents");
        String commentID = request.getParameter("commentID");
        mySQLServer.editArticleComment(Integer.valueOf(commentID), newArticleCommentContents);
        session.setAttribute("tempArticleId", articleID);
        return "redirect:/zhz/toArticle";
    }
}
