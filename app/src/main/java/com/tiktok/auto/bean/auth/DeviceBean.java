package com.tiktok.auto.bean.auth;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/24
 */
public class DeviceBean {
    /**
     * Id : 1
     * UserId : 1
     * AuthCode : 063e57c0-1009-4a33-b788-3b1b1f3e3094
     * DeviceId : 122231231313111
     * DeviceToken :
     * Channel :
     * Plat :
     * CreateTime : 0
     */

    private int Id;
    private int UserId;
    private String AuthCode;
    private String DeviceId;
    private String DeviceToken;
    private String Channel;
    private String Plat;
    private int CreateTime;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public void setAuthCode(String AuthCode) {
        this.AuthCode = AuthCode;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String DeviceToken) {
        this.DeviceToken = DeviceToken;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }

    public String getPlat() {
        return Plat;
    }

    public void setPlat(String Plat) {
        this.Plat = Plat;
    }

    public int getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(int CreateTime) {
        this.CreateTime = CreateTime;
    }
}