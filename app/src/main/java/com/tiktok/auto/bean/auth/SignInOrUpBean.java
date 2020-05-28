package com.tiktok.auto.bean.auth;

/**
 * Description: 登录注册
 * Author: zwb
 * Date: 2020/5/24
 */
public class SignInOrUpBean extends BaseAuthBean {
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

}
