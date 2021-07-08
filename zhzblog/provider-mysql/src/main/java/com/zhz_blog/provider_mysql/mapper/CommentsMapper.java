package com.zhz_blog.provider_mysql.mapper;

import com.zhz_blog.provider_mysql.pojo.ArticleComments;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Component
public interface CommentsMapper extends Mapper<ArticleComments> {
    void updateCommentContents(ArticleComments articleComments);
    List<ArticleComments> selectByArticleID(Integer articleID);
    void deleteByAccount(String account);
    void updateCommentOwner(String account,String newAccount);
    void deleteByArticleId(Integer articleId);
}
