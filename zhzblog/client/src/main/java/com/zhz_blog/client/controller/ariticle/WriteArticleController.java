package com.zhz_blog.client.controller.ariticle;

import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class WriteArticleController {
    @DubboReference
    private MySQLServer mySQLServer;
    @RequestMapping("/zhz/writeArticle")
    public String writeArticle(Model model, HttpSession session, HttpServletRequest request){
        String userAccount = (String) session.getAttribute("userAccount");
        model.addAttribute("userAccount",userAccount);
        String articleTitle = request.getParameter("articleTitle");
        //还没写或刷新页面时，没有articleTitle
        if(articleTitle==null)return "/article/writeArticle";
        String articleContent = request.getParameter("articleContent");
        //判断标题是否重复
        if(mySQLServer.ifArticleTitleRepetitive(userAccount,articleTitle,-1)){
            model.addAttribute("callback","文章标题重复");
            model.addAttribute("content",articleContent);
            return "/article/writeArticle";
        }
        mySQLServer.createNewArticle(userAccount,articleTitle,articleContent);
        //返回用户主界面
        return "redirect:/zhz/toUserMain";
    }
}
