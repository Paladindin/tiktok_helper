package com.zwb.auto.ui.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/1
 */
public class BaseAccessibilityService extends AccessibilityService {
    private AccessibilityManager mAccessibilityManager;

    private Context mContext;

//    public <T> Observable<T> bind$(Observable<T> paramObservable) {
//        return paramObservable.delay(1L, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }

    public boolean checkAccessibilityEnabled(String paramString) {
        Iterator<AccessibilityServiceInfo> iterator = this.mAccessibilityManager.getEnabledAccessibilityServiceList(16).iterator();
        while (iterator.hasNext()) {
            if (((AccessibilityServiceInfo)iterator.next()).getId().equals(paramString))
                return true;
        }
        return false;
    }

    public boolean clickTextViewByID(String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return false;
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo ani : list) {
                if (ani != null)
                    return performViewClick(ani);
            }
        return false;
    }

    public boolean clickTextViewByText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return false;
        List<AccessibilityNodeInfo> list = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                if (accessibilityNodeInfo != null)
                    return performViewClick(accessibilityNodeInfo);
            }
        return false;
    }

    public boolean clickTextViewByText(String paramString) {
        return clickTextViewByText(getRootInActiveWindow(), paramString);
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTextClass(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString1, String paramString2) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        if (paramAccessibilityNodeInfo == null)
            return arrayList;
        if (TextUtils.isEmpty(paramString2))
            return arrayList;
        for (AccessibilityNodeInfo accessibilityNodeInfo : paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString1)) {
            if (accessibilityNodeInfo != null && accessibilityNodeInfo.getClassName() != null && accessibilityNodeInfo.getClassName().equals(paramString2))
                arrayList.add(accessibilityNodeInfo);
        }
        return arrayList;
    }

    public AccessibilityNodeInfo findNodeByRect(List<AccessibilityNodeInfo> paramList, int paramInt1, int paramInt2) {
        int i;
        for (i = 0; i < paramList.size(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramList.get(i);
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            if (rect.contains(paramInt1, paramInt2))
                return accessibilityNodeInfo;
        }
        return null;
    }

    public AccessibilityNodeInfo findNodeInfosByClassName(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return null;
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return null;
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo1 = accessibilityNodeInfo.getChild(i);
            Log.e("---",accessibilityNodeInfo1.getClassName().toString());
            if (accessibilityNodeInfo1 != null && paramString.equals(accessibilityNodeInfo1.getClassName().toString()))
                return accessibilityNodeInfo1;
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        List<AccessibilityNodeInfo> list = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                if (accessibilityNodeInfo != null)
                    return accessibilityNodeInfo;
            }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString1, String paramString2) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        List<AccessibilityNodeInfo> list = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString1);
        if (list != null && !list.isEmpty())
            for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                if (accessibilityNodeInfo != null && accessibilityNodeInfo.getClassName().equals(paramString2))
                    return accessibilityNodeInfo;
            }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(String paramString) {
        return findViewByID(getRootInActiveWindow(), paramString);
    }

    public AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString, Boolean paramBoolean) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        List<AccessibilityNodeInfo> list = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
        if (list != null && !list.isEmpty())
            if (paramBoolean == null) {
                for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                    if (accessibilityNodeInfo != null)
                        return accessibilityNodeInfo;
                }
            } else {
                for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
                    if (accessibilityNodeInfo != null && accessibilityNodeInfo.isClickable() == paramBoolean.booleanValue())
                        return accessibilityNodeInfo;
                }
            }
        return null;
    }

    public AccessibilityNodeInfo findViewByText(String paramString) {
        return findViewByText(paramString, (Boolean)null);
    }

    public AccessibilityNodeInfo findViewByText(String paramString, Boolean paramBoolean) {
        return findViewByText(getRootInActiveWindow(), paramString, paramBoolean);
    }

    public void goAccess() {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(intent);
    }

    public void init(Context paramContext) {
        this.mContext = paramContext.getApplicationContext();
        this.mAccessibilityManager = (AccessibilityManager)this.mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    public boolean inputText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        // Byte code:
        //   0: aload_1
        //   1: ifnonnull -> 6
        //   4: iconst_0
        //   5: ireturn
        //   6: aload_2
        //   7: ifnull -> 21
        //   10: aload_2
        //   11: astore_3
        //   12: ldc 'null'
        //   14: aload_2
        //   15: invokevirtual equals : (Ljava/lang/Object;)Z
        //   18: ifeq -> 24
        //   21: ldc ''
        //   23: astore_3
        //   24: getstatic android/os/Build$VERSION.SDK_INT : I
        //   27: bipush #21
        //   29: if_icmplt -> 55
        //   32: new android/os/Bundle
        //   35: dup
        //   36: invokespecial <init> : ()V
        //   39: astore_2
        //   40: aload_2
        //   41: ldc 'ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE'
        //   43: aload_3
        //   44: invokevirtual putCharSequence : (Ljava/lang/String;Ljava/lang/CharSequence;)V
        //   47: aload_1
        //   48: ldc 2097152
        //   50: aload_2
        //   51: invokevirtual performAction : (ILandroid/os/Bundle;)Z
        //   54: ireturn
        //   55: getstatic android/os/Build$VERSION.SDK_INT : I
        //   58: bipush #18
        //   60: if_icmplt -> 103
        //   63: aload_0
        //   64: ldc 'clipboard'
        //   66: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
        //   69: checkcast android/content/ClipboardManager
        //   72: ldc 'label'
        //   74: aload_3
        //   75: invokestatic newPlainText : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Landroid/content/ClipData;
        //   78: invokevirtual setPrimaryClip : (Landroid/content/ClipData;)V
        //   81: aload_1
        //   82: iconst_1
        //   83: invokevirtual performAction : (I)Z
        //   86: ifeq -> 101
        //   89: aload_1
        //   90: ldc_w 32768
        //   93: invokevirtual performAction : (I)Z
        //   96: ifeq -> 101
        //   99: iconst_1
        //   100: ireturn
        //   101: iconst_0
        //   102: ireturn
        //   103: iconst_0
        //   104: ireturn
        return false;
    }

    public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {}

    @Override
    public void onInterrupt() {}

    public boolean performBackClick() {
        return performGlobalAction(GLOBAL_ACTION_BACK);
    }

    public void performScrollBackward() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
//        performGlobalAction(8192);
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    public boolean performScrollForward(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        return (paramAccessibilityNodeInfo == null) ? false : paramAccessibilityNodeInfo.performAction(4096);
    }

    public boolean performViewClick(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo;
        if (paramAccessibilityNodeInfo == null)
            return false;
        while (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isClickable())
                return accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            accessibilityNodeInfo = accessibilityNodeInfo.getParent();
        }
        return false;
    }

    public boolean performViewLongClick(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo;
        if (paramAccessibilityNodeInfo == null)
            return false;
        while (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isLongClickable())
                return accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
            accessibilityNodeInfo = accessibilityNodeInfo.getParent();
        }
        return false;
    }
}
