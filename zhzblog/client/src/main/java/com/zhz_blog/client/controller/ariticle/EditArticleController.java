package com.zhz_blog.client.controller.ariticle;

import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EditArticleController {
    @DubboReference
    private MySQLServer mySQLServer;

    @RequestMapping("/zhz/editArticle")
    public String editArticle(HttpServletRequest request, HttpSession session, Model model) {
        String aid = request.getParameter("articleId");
        if (aid == null) {
            //刷新了页面
            return "redirect:/zhz/toUserMain";
        }
        Integer articleId = Integer.valueOf(aid);
        String userAccount = (String) session.getAttribute("userAccount");
        String newArticleTitle = request.getParameter("newArticleTitle");
        if (newArticleTitle == null) {
            //前往修改页面
            Article article = mySQLServer.getArticle(articleId);
            model.addAttribute("userAccount", userAccount);
            model.addAttribute("articleTitle", article.getArticle_name());
            model.addAttribute("articleId", articleId);
            model.addAttribute("content", article.getArticle_content());
            return "/article/editArticle";
        }
        String newContent = request.getParameter("newContent");
        //判断标题是否有重复
        if (mySQLServer.ifArticleTitleRepetitive(userAccount, newArticleTitle, articleId)) {
            model.addAttribute("callback", "标题与您之前存在的标题重复");
            model.addAttribute("userAccount", userAccount);
            model.addAttribute("articleId", articleId);
            model.addAttribute("articleTitle", newArticleTitle);
            model.addAttribute("content", newContent);
            return "/article/editArticle";
        }
        //修改完成，进行写入
        mySQLServer.editArticle(articleId, newArticleTitle, newContent);
        return "redirect:/zhz/toUserMain";
    }
}
