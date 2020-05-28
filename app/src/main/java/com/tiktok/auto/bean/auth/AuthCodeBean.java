package com.tiktok.auto.bean.auth;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/24
 */
public class AuthCodeBean extends BaseAuthBean {


    /**
     * code : {"Id":3,"AuthCode":"063e57c0-1009-4a33-b788-3b1b1f3e3094","DeviceCount":10,"IsUse":false,"CreateTime":1590291337}
     * status : 200
     * time : 2020-05-24 11:35:37
     */

    private CodeBean code;

    public CodeBean getCode() {
        return code;
    }

    public void setCode(CodeBean code) {
        this.code = code;
    }


    public static class CodeBean {
        /**
         * Id : 3
         * AuthCode : 063e57c0-1009-4a33-b788-3b1b1f3e3094
         * DeviceCount : 10
         * IsUse : false
         * CreateTime : 1590291337
         */

        private int Id;
        private String AuthCode;
        private int DeviceCount;
        private boolean IsUse;
        private int CreateTime;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getAuthCode() {
            return AuthCode;
        }

        public void setAuthCode(String AuthCode) {
            this.AuthCode = AuthCode;
        }

        public int getDeviceCount() {
            return DeviceCount;
        }

        public void setDeviceCount(int DeviceCount) {
            this.DeviceCount = DeviceCount;
        }

        public boolean isIsUse() {
            return IsUse;
        }

        public void setIsUse(boolean IsUse) {
            this.IsUse = IsUse;
        }

        public int getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(int CreateTime) {
            this.CreateTime = CreateTime;
        }
    }
}
