package com.tiktok.auto.bean.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class CommentConfigBean {
    public List<String> commentContentList;
    public int commentCount;
    public int commentIntervalTime;
    public boolean isRandom;

    public CommentConfigBean(){
        init();
    }

    public void init(){
        commentContentList = new ArrayList<>();
        commentContentList.add("你好啊");
        commentContentList.add("不错啊");
        commentContentList.add("非常好");
        commentCount = 1;
        commentIntervalTime = 3000;
        isRandom = true;
    }
}
