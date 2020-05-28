package com.tiktok.auto.push;



public class MsgConfig {

    //客户端Id，多个设备用同一个clientId登录会互相踢下线，死循环
    //但是每次登录clientId不一样又不会收到离线消息
    static String clientId = "test" ;
    //用户编号 例如 u＿１
    static String topic = "test_user";
    // 设置超时时间，单位：秒
    static final int connectionTimeout = 30;
    // 心跳包发送间隔，单位：秒
    static final int keepAliveInterval = 5*60;
    //用户名
    static String userrname = "test";
    //密码
    static String password = "";
    //队列大小
    static final int maxInflight = 65536;

    static String HOST_IM = "tcp://106.12.98.18:1883";
//    static String HOST_IM = "tcp://192.168.1.11:1883";
    //mqtt服务器地址
    static final String[] serverURIs = new String[]{
//            "tcp://192.168.1.11:1883"
        "tcp://106.12.98.18:1883"
    };

    static void init(){
    }


    public static final int MSG_CONTENT_CONTRAL = 0x100;



}


