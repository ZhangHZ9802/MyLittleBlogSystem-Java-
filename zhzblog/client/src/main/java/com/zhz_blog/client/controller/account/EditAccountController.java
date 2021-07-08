package com.zhz_blog.client.controller.account;

import com.zhz_blog.api.service.MyRedisServer;
import com.zhz_blog.api.service.MySQLServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EditAccountController {
    @DubboReference
    private MySQLServer mySQLServer;
    @DubboReference
    private MyRedisServer myRedisServer;
    @RequestMapping("/zhz/editAccount")
    public String editAccount(HttpSession session, HttpServletRequest request, Model model){
        //获取消息
        String userAccount = (String) session.getAttribute("userAccount");
        String newUserAccount = request.getParameter("newUserAccount");
        if(newUserAccount==null){
            //直接访问/zhz/editAccount时
            model.addAttribute("userAccount",userAccount);
            return "/account/edit";
        }
        String userPassword = request.getParameter("userPassword");
        String newUserPassword1 = request.getParameter("newUserPassword1");
        String newUserPassword2 = request.getParameter("newUserPassword2");
        String newPassword = null;
        String newAccount = null;
        model.addAttribute("userAccount",userAccount);
        //修改参数
        if(!userPassword.equals("")){
            if(!mySQLServer.ifPasswordCorrect(userAccount,userPassword)){
                model.addAttribute("callback", "原始密码输入错误");
                return "/account/edit";
            }
            if(!newUserPassword1.equals("")&&newUserPassword1.equals(newUserPassword2)){
                newPassword = newUserPassword1;
            }else {
                model.addAttribute("callback", "新密码不能为空或两次新密码不一致");
                return "/account/edit";
            }
        }else if(!newUserPassword1.equals("")||!newUserPassword2.equals("")){
            model.addAttribute("callback", "请输入原密码");
            return "/account/edit";
        }
        if(!newUserAccount.equals("")){
            Integer userIDByAccount = mySQLServer.getUserIDByAccount(newUserAccount);
            if(userIDByAccount !=null&&
                    !userIDByAccount.equals(mySQLServer.getUserIDByAccount(userAccount))){
                model.addAttribute("callback", "新账号重复");
                return "/account/edit";
            }else{
                newAccount = newUserAccount;
            }
        }
        //更新数据库信息
        mySQLServer.updateUserAccount(userAccount,newAccount,newPassword);
        //删除session记录
        session.removeAttribute("userAccount");
        //删除Redis注册，返回登录界面
        myRedisServer.removeAccount(userAccount);
        return "redirect:/login";
    }
}
