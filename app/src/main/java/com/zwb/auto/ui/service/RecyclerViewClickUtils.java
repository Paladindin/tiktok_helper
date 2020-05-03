package com.zwb.auto.ui.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.zwb.auto.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
public class RecyclerViewClickUtils {

    private int curromMax = 1;

    public List<AccessibilityNodeInfo> items = null;

    private Boolean next = Boolean.TRUE;

    public AccessibilityNodeInfo parent = null;

    private RunListener runListener;

    public AccessibilityService service;

    private String tag;

    private List<String> tags = new ArrayList<String>();

    public RecyclerViewClickUtils(RunListener paramRunListener, int paramInt) {
        this.items = null;
        this.curromMax = paramInt;
        this.runListener = paramRunListener;
        this.parent = paramRunListener.getParentInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void clickView(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        Rect rect = new Rect();
        paramAccessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo(((rect.left + rect.right) / 2), ((rect.top + rect.bottom) / 2));
        this.service.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 100L, 350L)).build(), null, null);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doBatch() throws Exception {
        if (!isEmptyArray(this.items) && (App.getInstance()).getStartRun() && this.tags.size() < this.curromMax) {
            List<AccessibilityNodeInfo> list = this.items;
            boolean bool = false;
            AccessibilityNodeInfo accessibilityNodeInfo = list.remove(0);
            if (accessibilityNodeInfo != null) {
                boolean bool1;
                Object[] arrayOfObject = this.runListener.solveTag(accessibilityNodeInfo, this.tags);
                if (arrayOfObject != null) {
                    this.tag = (String)arrayOfObject[1];
                } else {
                    this.tag = null;
                }
                if (!TextUtils.isEmpty(this.tag) && !this.tags.contains(this.tag)) {
                    bool1 = true;
                } else {
                    bool1 = false;
                }
                this.next = bool1;
                if (this.next) {
                    AccessibilityNodeInfo accessibilityNodeInfo1 = (AccessibilityNodeInfo)arrayOfObject[0];
                    if (accessibilityNodeInfo1 != null)
                        if (accessibilityNodeInfo1.isClickable()) {
                            bool1 = bool;
                            if (this.next) {
                                bool1 = bool;
                                if (accessibilityNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK))
                                    bool1 = true;
                            }
                            this.next = bool1;
                        } else if (Build.VERSION.SDK_INT >= 24) {
                            if (accessibilityNodeInfo1.isVisibleToUser()) {
                                clickView(accessibilityNodeInfo1);
                            } else {
                                this.next = Boolean.valueOf(false);
                            }
                        } else {
                            return;
                        }
                }
                if (this.next) {
                    String str = this.tag;
                    if (str != null)
                        this.tags.add(str);
                    Thread.sleep(1000L);
                    this.runListener.next();
//                    Thread.sleep((App.getInstance()).config.getSendInterval());
                }
            }
            if (this.runListener.runNext(this.tags.size())) {
                doBatch();
                return;
            }
            return;
        }
        start();
    }

    private boolean isEmptyArray(List paramList) {
        return (paramList == null || paramList.size() == 0);
    }

    private List<AccessibilityNodeInfo> getChildParent(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        ArrayList<AccessibilityNodeInfo> arrayList2 = new ArrayList();
        ArrayList<AccessibilityNodeInfo> arrayList1 = arrayList2;
        if (paramAccessibilityNodeInfo != null) {
            arrayList1 = arrayList2;
            if (paramAccessibilityNodeInfo.getChildCount() > 0) {
                arrayList2 = new ArrayList();
                int i = 0;
                while (true) {
                    arrayList1 = arrayList2;
                    if (i < paramAccessibilityNodeInfo.getChildCount()) {
                        if (paramAccessibilityNodeInfo.getChild(i) != null)
                            arrayList2.add(paramAccessibilityNodeInfo.getChild(i));
                        i++;
                        continue;
                    }
                    break;
                }
            }
        }
        return arrayList1;
    }

    public RunListener getRunListener() {
        return this.runListener;
    }

    public void setService(AccessibilityService paramAccessibilityService) {
        this.service = paramAccessibilityService;
    }

    public void solveError() {
        this.curromMax++;
    }

    public void start() throws Exception {
        if ((App.getInstance()).getStartRun() && this.tags.size() < this.curromMax) {
            if (this.parent == null)
                return;
            this.next = Boolean.TRUE;
            if (this.items != null) {
                this.next = this.parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                if (!this.next) {
                    Thread.sleep(500L);
                    this.parent = this.runListener.getParentInfo();
                    this.next = this.parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }
                Thread.sleep(1500L);
            }
            this.parent = this.runListener.getParentInfo();
            this.items = getChildParent(this.parent);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("tag==start==parent count==");
            stringBuilder.append(this.items.size());
            Log.e("qyh", stringBuilder.toString());
            if (!isEmptyArray(this.items) && this.next) {
                doBatch();
                return;
            }
            if (!this.runListener.runNext(this.tags.size()));
        }
    }

    public static interface RunListener {
        AccessibilityNodeInfo getParentInfo();

        void next() throws Exception;

        Boolean runNext(int param1Int) throws Exception;

        Object[] solveTag(AccessibilityNodeInfo param1AccessibilityNodeInfo, List<String> param1List) throws Exception;
    }
}
