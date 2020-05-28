package com.tiktok.auto.push;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tiktok.auto.bean.control.ControlConfigBean;
import com.tiktok.auto.config.ConfigManager;


public class MsgPresenter {

    private static String TAG = "MsgPresenter";

    private static final MsgPresenter ourInstance = new MsgPresenter();

    public static MsgPresenter getInstance() {
        return ourInstance;
    }

    private MsgPresenter() {
    }

    public void receiveMsg(byte[] msgBytes) throws Exception {
        //1.解析
        MsgBean msgBean = new Gson().fromJson(new String(msgBytes, "utf-8"), MsgBean.class);
        switch (msgBean.getMsg_type()) {
            case MsgConfig.MSG_CONTENT_CONTRAL:
                //处理单聊信息
                handleControlMsg(msgBean);
                break;
        }
    }

    //备注，自己会收到自己发出去的消息，所以发送消息时不需要做数据的存储更新，否则会对消息进行多次处理
    public void sendMsg(MsgBean msg) {
        //群控
        if (msg.getMsg_type() == MsgConfig.MSG_CONTENT_CONTRAL) {
            MsgService.publish(getMsg(msg), msg.getTo_id(), 2);
        }
    }

    public void sendMsgSuccess(byte[] msgBytes) {

    }

    public byte[] getMsg(MsgBean msgBean) {
        return new Gson().toJson(msgBean).getBytes();
    }

    private long check_time = System.currentTimeMillis();

    public void check_time(Context context) {
        if ((System.currentTimeMillis() - check_time) > 5 * 1000) {
            check_time = System.currentTimeMillis();
            MsgService.start(context);
        }
    }

    private void handleControlMsg(MsgBean msg) {
        Log.e(TAG, "receiveMsg: 消息处理成功 " + new Gson().toJson(msg));
        ControlConfigBean controlConfigBean = new ControlConfigBean();
//        controlConfigBean.init();
        ConfigManager.getInstance().saveControlConfig(controlConfigBean);
    }
}
