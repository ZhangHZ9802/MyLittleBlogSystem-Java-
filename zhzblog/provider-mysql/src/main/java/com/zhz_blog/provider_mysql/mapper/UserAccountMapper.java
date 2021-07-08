package com.zhz_blog.provider_mysql.mapper;

import com.zhz_blog.provider_mysql.pojo.UserAccount;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;


@org.apache.ibatis.annotations.Mapper
@Component
public interface UserAccountMapper extends Mapper<UserAccount> {
    String getPassword(String account);
    void deleteAccount(String account);
    void updateUserAccount(String account,String newAccount);
    void updateUserPassword(String account,String newPassword);
    Integer selectUserIDByAccount(String account);
}
