package com.tiktok.auto.push;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.tuolu.subtitle.bean.UserManager;
import com.tiktok.auto.utils.ServiceUtil;
import com.tiktok.auto.utils.ThreadUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.nio.charset.StandardCharsets;


public class MsgService extends Service {

    public static void start(Context context) {
        //启动IM服务
        if (!ServiceUtil.isServiceRunning(context, "MsgService")) {
            context.startService(new Intent(context, MsgService.class));
        } else {
            Log.e(TAG, "start: MsgService is running !");
        }
    }

    @Subscribe
    public void update(MsgEvent event){
        if (event.getStatus() == MsgEvent.STATUS_SENDING){
            presenter.sendMsg(event.msg);
        }
    }

    private static String TAG = "MsgService";

    private MsgPresenter presenter;
    //封装好的MQTTClient供操作，发送和接手等设置都用它
    @SuppressLint("StaticFieldLeak")
    private static MqttAndroidClient client;
    //MQTT连接参数设置
    private MqttConnectOptions conOpt;
    private NetReceiver netReceiver;
    private static boolean isCloseService = false;

    public MsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        startForeground(0, null);
        MsgConfig.init();
        init();
        presenter = MsgPresenter.getInstance();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onCreate();
    }

    /**
     * 发布主题消息：
     * qos：服务质量
     * qos=0"至多一次"，消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。
     * qos=1"至少一次"，确保消息到达，但消息重复可能会发生。
     * qos=2"只有一次"，确保消息到达一次。
     * retained是否在服务器保留断开连接后的最后一条消息
     * msg：消息内容
     **/
    public static void publish(byte[] msg, String topic, int qos) {
        if (UserManager.getInstance().getId() < 0) {
            Log.e(TAG, "publish: please login ahead ! ");
        } else {
            //确保不是发给自己的消息
//            if (!topic.equals("u" + UserManager.getInstance().getId())) {
                try {
                    Log.e(TAG, "publish: " + new String(msg));
                    client.publish(topic, msg, qos, false);
                } catch (MqttException e) {
                    Log.e(TAG, "publish: " + e.getMessage());
                }
//            } else {
//                Log.e(TAG, "publish: err send 2 self !");
//            }
        }
    }

    /**
     * 订阅主题
     **/
    public static void subscribe(String[] topicName, int[] qos) {
        if (client != null && client.isConnected()) {
            try {
                /**
                 * 订阅（订阅话题和服务质量以数组方式传递，
                 * 订阅话题：String arr []={"global_notification"};
                 * 服务质量int qos []={2};）
                 **/
                client.subscribe(topicName, qos, null, new IMqttActionListener() {
                    //订阅成功
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        String[] topics = asyncActionToken.getTopics();
                        for (String topic : topics) {
                            Log.e(TAG, "订阅成功 " + topic);
                        }
                    }

                    //订阅失败
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "订阅失败 " + exception.getLocalizedMessage());
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static void subscribe() {
        if (UserManager.getInstance().getId() >= 0) {
            Log.e(TAG, "subscribe: 开始订阅主题 ");
            String[] arr = new String[2];
            //接收官方消息的topic
            arr[0] = "0";
            //自己的topic
            arr[1] = "u" + UserManager.getInstance().getId();
            //服务质量
            int[] qos = new int[arr.length];
            for (int i = 0; i < qos.length; i++) {
                qos[i] = 2;
            }
            //执行订阅方法
            Log.e(TAG, "subscribe: 订阅主题" + new Gson().toJson(arr));
            subscribe(arr, qos);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        // 服务器地址（协议+地址+端口号）
        client = new MqttAndroidClient(getApplicationContext(), MsgConfig.HOST_IM, "u" + UserManager.getInstance().getId());
        // 设置MQTT监听并且接受消息
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //断开连接必须重新订阅才能收到消息
                if (reconnect) {
                    subscribe();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "connectionLost" + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                if (presenter == null) {
                    Log.i(TAG, "messageArrived: presenter is null !");
                    presenter = MsgPresenter.getInstance();
                }
                ThreadUtil.startThreadInPool(new ThreadUtil.Task() {
                    @Override
                    public void doTask() {
                        Log.e(TAG, "messageArrived: 开始处理消息数据！");
                        try {
                            presenter.receiveMsg(message.getPayload());
                        } catch (Exception e) {
                            Log.e(TAG, "doTask: " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                try {
                    Log.i(TAG, "deliveryComplete: 消息发送成功 ！ " + new String(token.getMessage().getPayload(), StandardCharsets.UTF_8));
                } catch (MqttException e) {
                    Log.e(TAG, "deliveryComplete: error " + e.getMessage());
                }
            }
        });
        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(false);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(MsgConfig.connectionTimeout);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(MsgConfig.keepAliveInterval);
        // 用户名
        conOpt.setUserName(MsgConfig.userrname);
        // 密码
        conOpt.setPassword(MsgConfig.password.toCharArray());
        //设置自动重连
        conOpt.setAutomaticReconnect(true);
        //队列大小
        conOpt.setMaxInflight(MsgConfig.maxInflight);
        //服务器列表
        conOpt.setServerURIs(MsgConfig.serverURIs);
        //监听网络状态
        netReceiver = new NetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkNet();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(netReceiver);
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

        try {
            if (client != null) {
                //断开连接
                client.disconnect();
                //关闭释放资源
                client.unregisterResources();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        if (isCloseService) {
            isCloseService = false;
            //完全退出
            Log.e("完全退出", "true");
        } else {
            //服务停止，重新开启服务。
            Log.e("服务已被杀死", "false");
            stopForeground(true);
            MsgService.start(getApplicationContext());
        }

    }

    public static void closeConnect() {
        isCloseService = true;
    }

    /**
     * 连接MQTT服务器
     * client．isConnected　：　client是否在线 （true，false）
     */
    private void doClientConnection() {

        if (!client.isConnected()) {
            try {
                client.connect(conOpt, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.e(TAG, "onSuccess: 连接成功 ");
                        subscribe();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable arg1) {
                        Log.e(TAG, "onFailure: " + arg1.getMessage());
                        Log.e(TAG, "onFailure: " + arg1.getLocalizedMessage());
                        Log.e(TAG, "onFailure: " + arg1.getCause());
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 判断网络是否连接
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                doClientConnection();
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
            }

        });
    }

    private class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            doClientConnection();
        }
    }

}
