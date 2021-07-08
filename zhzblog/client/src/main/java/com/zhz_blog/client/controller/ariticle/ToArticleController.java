package com.zhz_blog.client.controller.ariticle;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import com.zhz_blog.common.commonClass.Article;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ToArticleController {
    @DubboReference
    private MySQLServer mySQLServer;
    @DubboReference
    private MyRedisServer myRedisServer;
    @RequestMapping("/zhz/toArticle")
    public String toArticle(HttpSession session, HttpServletRequest request, Model model){
        String aID = request.getParameter("articleID");
        //非正常点击文章链接的访问
        if(aID==null){
            String tempArticleId = (String) session.getAttribute("tempArticleId");
            if (tempArticleId==null){
                //非法访问
                return "redirect:/zhz/toUserMain";
            }else {
                //从其它地方redirect来会有tempArticleId
                aID = tempArticleId;
                session.removeAttribute("tempArticleId");
            }
        }
        Integer articleID = Integer.valueOf(aID);
        String userAccount = (String) session.getAttribute("userAccount");
        model.addAttribute("userAccount",userAccount);
        //获取并显示文章内容
        Article article = mySQLServer.getArticle(articleID);
        if(article==null)return "redirect:/zhz/toUserMain";//在获取之前文章被删除
        model.addAttribute("article", article);
        //获取并显示文章获赞数
        model.addAttribute("countOfLikes",myRedisServer.getArticleLikedCounts(articleID));
        //获取并显示文章评论内容
        model.addAttribute("articleComments",mySQLServer.getArticleComments(articleID));
        return "/article/article";
    }
}
