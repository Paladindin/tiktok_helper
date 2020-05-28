package com.tiktok.auto.bean.auth;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/24
 */
public class UserBean  {


    /**
     * Id : 2
     * Username : test1234
     * Pwd : test1234
     * AuthCode :
     * CreateTime : 1590291132
     * LoginTime : 1590291132
     * BanTime : 0
     * IsBan : false
     * BanUserId : 0
     * BanMsg :
     */

    private int Id;
    private String Username;
    private String Pwd;
    private String AuthCode;
    private int CreateTime;
    private int LoginTime;
    private int BanTime;
    private boolean IsBan;
    private int BanUserId;
    private String BanMsg;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String Pwd) {
        this.Pwd = Pwd;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public void setAuthCode(String AuthCode) {
        this.AuthCode = AuthCode;
    }

    public int getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(int CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(int LoginTime) {
        this.LoginTime = LoginTime;
    }

    public int getBanTime() {
        return BanTime;
    }

    public void setBanTime(int BanTime) {
        this.BanTime = BanTime;
    }

    public boolean isIsBan() {
        return IsBan;
    }

    public void setIsBan(boolean IsBan) {
        this.IsBan = IsBan;
    }

    public int getBanUserId() {
        return BanUserId;
    }

    public void setBanUserId(int BanUserId) {
        this.BanUserId = BanUserId;
    }

    public String getBanMsg() {
        return BanMsg;
    }

    public void setBanMsg(String BanMsg) {
        this.BanMsg = BanMsg;
    }
}