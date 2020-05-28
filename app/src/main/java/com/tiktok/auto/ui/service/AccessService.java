package com.tiktok.auto.ui.service;

import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.tiktok.auto.App;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/1
 */
public class AccessService extends BaseAccessibilityService {

    private static AccessService mInstance;

    private Handler handler = new Handler();

    static App instance;

    public static AccessService get() {
        if (mInstance == null)
            mInstance = new AccessService();
        return mInstance;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e("zwb", event.toString());
        int eventType = event.getEventType();
//        CharSequence packageName = event.getPackageName();
//        if (eventType == TYPE_WINDOW_STATE_CHANGED){
//            Log.e("zwb","clickTextViewByID");
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    clickTextViewByID("com.ss.android.ugc.aweme:id/ak5");
//                }
//            },500);
//
//        }
        if (eventType != TYPE_WINDOW_STATE_CHANGED && eventType != TYPE_WINDOW_CONTENT_CHANGED) {
            if (eventType != TYPE_VIEW_SCROLLED)
                return;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("event==");
            stringBuilder.append(event.toString());
//            Log.e("zwb", stringBuilder.toString());
            return;
        }
        if (App.getInstance().getService() == null)
            App.getInstance().setService(this);
    }

    public void onInterrupt() {}

    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("zwb", "douyin2==========");
    }
}
