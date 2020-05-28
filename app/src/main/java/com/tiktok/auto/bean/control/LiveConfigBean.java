package com.tiktok.auto.bean.control;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class LiveConfigBean {
    public int starCount;
    public int commentCount;
    public int giftCount;
    public int intervalTime;
    public int fansLimit;
    public int stayTime;

    public LiveConfigBean(){
        init();
    }

    public void init(){
        starCount = 1;
        commentCount = 1;
        giftCount = 1;
        intervalTime = 1;
        fansLimit = 50;
        stayTime = 10 * 1000;
    }
}
