package com.tiktok.auto.bean.auth;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/24
 */
public class BindBean extends BaseAuthBean {


    /**
     * device : {"Id":1,"UserId":1,"AuthCode":"063e57c0-1009-4a33-b788-3b1b1f3e3094","DeviceId":"122231231313111","DeviceToken":"","Channel":"","Plat":"","CreateTime":0}
     */

    private DeviceBean device;

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }


}
