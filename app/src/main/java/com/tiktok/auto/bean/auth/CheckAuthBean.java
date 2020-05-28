package com.tiktok.auto.bean.auth;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/24
 */
public class CheckAuthBean extends BaseAuthBean {

    private DeviceBean devices;

    public DeviceBean getDevice() {
        return devices;
    }

    public void setDevice(DeviceBean device) {
        this.devices = device;
    }
}
