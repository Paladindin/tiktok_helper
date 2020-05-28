package com.tiktok.auto.bean.control;

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/25
 */
public class ControlConfigBean {
    public TrainConfigBean trainConfig;
    public CommentConfigBean commentConfig;
    public PreciseConfigBean preciseConfig;
    public LiveConfigBean liveConfig;
    public BatchConfigBean batchConfig;

    public ControlConfigBean(){
        init();
    }

    public void init() {
        trainConfig = new TrainConfigBean();
//        trainConfig.init();
        commentConfig = new CommentConfigBean();
//        commentConfig.init();
        preciseConfig = new PreciseConfigBean();
//        preciseConfig.init();
        liveConfig = new LiveConfigBean();
//        liveConfig.init();
        batchConfig = new BatchConfigBean();
//        batchConfig.init();
    }
}
