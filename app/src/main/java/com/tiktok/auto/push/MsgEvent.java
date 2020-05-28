package com.tiktok.auto.push;


public class MsgEvent {

    //该事件只负责消息的收发，不负责具体的消息类型处理

    public static int STATUS_SENDING = 0;
    public static int STATUS_SEND_UCCESS = 1;
    public static int STATUS_SEND_FAIL = 2;
    public static int RECEIVE = 3 ;

    int status ;
    MsgBean msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }
}

