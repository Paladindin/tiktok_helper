package com.tiktok.auto.bean.control;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class TrainConfigBean {

    public int videoScanTime;
    public int videoSwitchTime;
    public int commentCount;
    public int starCount;
    public int giftCount;
    public int targetIntervalTime;
    public String city;

    public TrainConfigBean(){
        init();
    }

    public void init(){
        videoScanTime = 5000;
        videoSwitchTime = 4000;
        commentCount = 1;
        starCount = 1;
        giftCount = 1;
        targetIntervalTime = 3000;
        city = "武汉";
    }
}
