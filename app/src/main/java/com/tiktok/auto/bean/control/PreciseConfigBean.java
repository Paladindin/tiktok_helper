package com.tiktok.auto.bean.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class PreciseConfigBean {
    public String keyword;
    public List<String> commentContentList;
    public int msgCount;
    public int msgIntervalTime;
    public int sameUserCount;
    public int starBeginIndex;
    public int starEndIndex;

    public PreciseConfigBean(){
        init();
    }

    public void init(){
        keyword = "美景";
        commentContentList = new ArrayList<>();
        commentContentList.add("不错啊");
        commentContentList.add("非常好");
        commentContentList.add("666666");
        msgCount = 1;
        msgIntervalTime = 3000;
        sameUserCount = 1;
        starBeginIndex = 1;
        starEndIndex = 50;
    }
}
