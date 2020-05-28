package com.tiktok.auto.push;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tuolu.subtitle.bean.UserManager;


public class MsgBean implements MultiItemEntity {
    public long id;
    String msgId ;
    //消息类型
    int  msg_type  ;
    //发送方
    String from_id  ;
    //接收方
    String to_id ;
    //发送时间
    long create_time  ;
    //消息传输数据的类型
    int content_type  ;
    //传输的内容
    String content ;
    //信令内容
    int ack ;
    //发送成功，失败
    boolean success ;

    @Override
    public int getItemType() {
        if (from_id.equals("u"+ UserManager.getInstance().getId())){
            //自己发的消息
            switch (content_type){
                case 1 : return 0; //文本
                case 2 : return 1; //图片
                case 3 : return 3; //视频
                case 4 : return 2; //音频
            }
        }else {
            //别人发的消息
            switch (content_type){
                case 1 : return 100;
                case 2 : return 101;
                case 3 : return 103;
                case 4 : return 102;
            }
        }
        return 0;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
