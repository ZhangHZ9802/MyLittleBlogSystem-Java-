package com.zhz_blog.provider_mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    @Id
    @KeySql(useGeneratedKeys = true)//自增键回填
    private Integer user_id;
    private String user_account;
    private String user_password;

    public UserAccount(String user_account,String user_password){
        this.user_account = user_account;
        this.user_password = user_password;
    }
}
