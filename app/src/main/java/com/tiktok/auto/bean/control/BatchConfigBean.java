package com.tiktok.auto.bean.control;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class BatchConfigBean {
    public int msgCount;
    public int fansLimit;
    public int intervalTime;
    private String content;

    public BatchConfigBean(){
        init();
    }
    public void init(){
        msgCount = 1;
        fansLimit = 50;
        intervalTime = 3000;
        content = "hi,你好啊";
    }
}
