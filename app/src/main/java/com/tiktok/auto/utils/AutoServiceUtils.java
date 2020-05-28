package com.tiktok.auto.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.tiktok.auto.ui.service.AccessService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AutoServiceUtils {
    public static Rect mRecycleRect = new Rect();

    private boolean isEmptyArray(List paramList) {
        return (paramList == null || paramList.size() == 0);
    }

    public static boolean goBack(AccessibilityService service){
        if (service != null){
            return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }else {
            return false;
        }
    }


    public static boolean clickView(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (paramAccessibilityNodeInfo != null) {
            if (paramAccessibilityNodeInfo.isClickable())
                return paramAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            paramAccessibilityNodeInfo = paramAccessibilityNodeInfo.getParent();
            if (paramAccessibilityNodeInfo != null) {
                boolean bool = clickView(paramAccessibilityNodeInfo);
                paramAccessibilityNodeInfo.recycle();
                if (bool)
                    return true;
            }
        }
        return false;
    }

    public static boolean clickView2(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (paramAccessibilityNodeInfo != null) {
            if (paramAccessibilityNodeInfo.isClickable())
                return paramAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            paramAccessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(0);
            if (paramAccessibilityNodeInfo != null) {
                boolean bool = clickView2(paramAccessibilityNodeInfo);
                paramAccessibilityNodeInfo.recycle();
                if (bool)
                    return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void scrollView(AccessibilityService paramAccessibilityService, AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        Rect rect = new Rect();
        paramAccessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
        path.lineTo((rect.left + rect.right) / 2, rect.top);
        paramAccessibilityService.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 10L, 200L)).build(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean scrollViewVertical(AccessibilityService paramAccessibilityService, int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, 0);
        return paramAccessibilityService.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 0, 200L)).build(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean scrollViewVertical(AccessibilityService paramAccessibilityService, int x, int startY,int endY) {
        Path path = new Path();
        path.moveTo(x, startY);
        path.lineTo(x, endY);
        return paramAccessibilityService.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 0, 200L)).build(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean scrollViewHorizontal(AccessibilityService paramAccessibilityService, int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(0, y);
        return paramAccessibilityService.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 0, 100L)).build(), null, null);
    }



    public static List<AccessibilityNodeInfo> filterErrData(List<AccessibilityNodeInfo> paramList) {
        if (Utils.isEmptyArray(paramList))
            return null;
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        for (AccessibilityNodeInfo accessibilityNodeInfo : paramList) {
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            accessibilityNodeInfo.getBoundsInParent(new Rect());
            if (rect.left > 1 && rect.left != rect.right)
                arrayList.add(accessibilityNodeInfo);
        }
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findAllView(AccessibilityService paramAccessibilityService) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return arrayList;
        findViewByClassName(arrayList, accessibilityNodeInfo);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByClassName(AccessibilityService paramAccessibilityService, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return arrayList;
        findViewByClassName(arrayList, accessibilityNodeInfo, paramString);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByClassName(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        findViewByClassName(arrayList, paramAccessibilityNodeInfo, paramString);
        paramAccessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static void findViewByClassName(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (paramAccessibilityNodeInfo == null)
            return;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                paramList.add(accessibilityNodeInfo);
                findViewByClassName(paramList, accessibilityNodeInfo);
                accessibilityNodeInfo.recycle();
            }
        }
    }

    public static void findViewByClassName(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null)
                if (paramString.equals(accessibilityNodeInfo.getClassName().toString())) {
                    paramList.add(accessibilityNodeInfo);
                } else {
                    findViewByClassName(paramList, accessibilityNodeInfo, paramString);
                    accessibilityNodeInfo.recycle();
                }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByClassNameDesc(AccessibilityService paramAccessibilityService, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return arrayList;
        findViewByClassNameInFrame(arrayList, accessibilityNodeInfo, paramString);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static void findViewByClassNameInFrame(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return;
        int j = getChildCount(paramAccessibilityNodeInfo);
        for (int i = 0; i < j; i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null)
                if (paramString.equals(accessibilityNodeInfo.getClassName().toString())) {
                    paramList.add(accessibilityNodeInfo);
                } else {
                    findViewByClassName(paramList, accessibilityNodeInfo, paramString);
                    accessibilityNodeInfo.recycle();
                }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByClassNameInGroup(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        findViewByClassName(arrayList, paramAccessibilityNodeInfo, paramString);
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByContainsText(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                paramAccessibilityService.getWindows();
            }
            return null;
        }
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
        accessibilityNodeInfo.recycle();
        return list;
    }

    public static List<AccessibilityNodeInfo> findViewByContainsText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        return paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
    }

    public static List<AccessibilityNodeInfo> findViewByContentDescription(AccessibilityService paramAccessibilityService, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return arrayList;
        findViewByContentDescription(arrayList, accessibilityNodeInfo, paramString);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByContentDescription(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        if (paramAccessibilityNodeInfo == null)
            return arrayList;
        findViewByContentDescription(arrayList, paramAccessibilityNodeInfo, paramString);
        paramAccessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static void findViewByContentDescription(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                CharSequence charSequence = accessibilityNodeInfo.getContentDescription();
                if (charSequence != null && charSequence.toString().contains(paramString)) {
                    paramList.add(accessibilityNodeInfo);
                } else {
                    findViewByContentDescription(paramList, accessibilityNodeInfo, paramString);
                    accessibilityNodeInfo.recycle();
                }
            }
        }
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsDescription(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        if (paramAccessibilityNodeInfo == null)
            return arrayList;
        findViewByEquialsDescription(arrayList, paramAccessibilityNodeInfo, paramString);
        paramAccessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityService paramAccessibilityService, String paramString) {
        List<AccessibilityNodeInfo> list = findViewByContainsText(paramAccessibilityService, paramString);
        if (Utils.isEmptyArray(list))
            return null;
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
            if (accessibilityNodeInfo.getText() != null && paramString.equals(accessibilityNodeInfo.getText().toString())) {
                arrayList.add(accessibilityNodeInfo);
                continue;
            }
            accessibilityNodeInfo.recycle();
        }
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityService paramAccessibilityService, String text, String paramString2) {
        List<AccessibilityNodeInfo> list = findViewByContainsText(paramAccessibilityService, text);
        if (Utils.isEmptyArray(list))
            return null;
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
            if (accessibilityNodeInfo.getText() != null && text.equals(accessibilityNodeInfo.getText().toString()) && paramString2.equals(accessibilityNodeInfo.getClassName())) {
                arrayList.add(accessibilityNodeInfo);
                continue;
            }
            accessibilityNodeInfo.recycle();
        }
        return arrayList;
    }

    public static List<AccessibilityNodeInfo> findViewByEqualsText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        List<AccessibilityNodeInfo> list = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
        if (Utils.isEmptyArray(list))
            return null;
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        for (AccessibilityNodeInfo accessibilityNodeInfo : list) {
            if (accessibilityNodeInfo.getText() != null && paramString.equals(accessibilityNodeInfo.getText().toString())) {
                arrayList.add(accessibilityNodeInfo);
                continue;
            }
            accessibilityNodeInfo.recycle();
        }
        return arrayList;
    }


    public static void findViewByEquialsDescription(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                CharSequence charSequence = accessibilityNodeInfo.getContentDescription();
                if (charSequence != null && charSequence.toString().equals(paramString)) {
                    paramList.add(accessibilityNodeInfo);
                } else {
                    findViewByEquialsDescription(paramList, accessibilityNodeInfo, paramString);
                    accessibilityNodeInfo.recycle();
                }
            }
        }
    }

    public static AccessibilityNodeInfo findViewByFirstClassName(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo1 = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo1 == null)
            return null;
        AccessibilityNodeInfo accessibilityNodeInfo2 = findViewByFirstClassName(accessibilityNodeInfo1, paramString);
        accessibilityNodeInfo1.recycle();
        return accessibilityNodeInfo2;
    }

    public static AccessibilityNodeInfo findViewByFirstClassName(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                if (paramString.equals(accessibilityNodeInfo.getClassName().toString()))
                    return accessibilityNodeInfo;
                AccessibilityNodeInfo accessibilityNodeInfo1 = findViewByFirstClassName(accessibilityNodeInfo, paramString);
                accessibilityNodeInfo.recycle();
                if (accessibilityNodeInfo1 != null)
                    return accessibilityNodeInfo1;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByFirstContainsContentDescription(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo1 = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo1 == null)
            return null;
        AccessibilityNodeInfo accessibilityNodeInfo2 = findViewByFirstContainsContentDescription(accessibilityNodeInfo1, paramString);
        accessibilityNodeInfo1.recycle();
        return accessibilityNodeInfo2;
    }

    public static AccessibilityNodeInfo findViewByFirstContainsContentDescription(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                CharSequence charSequence = accessibilityNodeInfo.getContentDescription();
                if (charSequence != null && charSequence.toString().contains(paramString))
                    return accessibilityNodeInfo;
                AccessibilityNodeInfo accessibilityNodeInfo1 = findViewByFirstContainsContentDescription(accessibilityNodeInfo, paramString);
                accessibilityNodeInfo.recycle();
                if (accessibilityNodeInfo1 != null)
                    return accessibilityNodeInfo1;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByFirstEqualsContentDescription(AccessibilityService paramAccessibilityService, String paramString) {
        AccessibilityNodeInfo accessibilityNodeInfo1 = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo1 == null)
            return null;
        AccessibilityNodeInfo accessibilityNodeInfo2 = findViewByFirstEqualsContentDescription(accessibilityNodeInfo1, paramString);
        accessibilityNodeInfo1.recycle();
        return accessibilityNodeInfo2;
    }

    public static AccessibilityNodeInfo findViewByFirstEqualsContentDescription(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        if (paramAccessibilityNodeInfo == null)
            return null;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null && accessibilityNodeInfo.isVisibleToUser()) {
                CharSequence charSequence = accessibilityNodeInfo.getContentDescription();
                if (charSequence != null && paramString.equals(charSequence.toString()))
                    return accessibilityNodeInfo;
                AccessibilityNodeInfo accessibilityNodeInfo1 = findViewByFirstEqualsContentDescription(accessibilityNodeInfo, paramString);
                accessibilityNodeInfo.recycle();
                if (accessibilityNodeInfo1 != null)
                    return accessibilityNodeInfo1;
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewById(AccessibilityService paramAccessibilityService, String paramString) {
        List<AccessibilityNodeInfo> list = findViewByIdList(paramAccessibilityService, paramString);
        return Utils.isEmptyArray(list) ? null : list.get(0);
    }

    public static AccessibilityNodeInfo findViewById(AccessibilityNodeInfo accessibilityNodeInfo, String paramString) {
        if (accessibilityNodeInfo == null)
            return null;
        List<AccessibilityNodeInfo> nodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
        return Utils.isEmptyArray(nodeInfosByViewId) ? null : nodeInfosByViewId.get(0);
    }

    public static AccessibilityNodeInfo findViewById(AccessibilityService paramAccessibilityService, String paramString1, String paramString2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString1);
        stringBuilder.append(":id/");
        stringBuilder.append(paramString2);
        return findViewById(paramAccessibilityService, stringBuilder.toString());
    }

    public static List<AccessibilityNodeInfo> findViewByIdList(AccessibilityService paramAccessibilityService, String paramString) {
        try {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
            if (accessibilityNodeInfo == null)
                return null;
            List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(paramString);
            accessibilityNodeInfo.recycle();
            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static List<AccessibilityNodeInfo> findViewByRect(AccessibilityService paramAccessibilityService, Rect paramRect) {
        ArrayList<AccessibilityNodeInfo> arrayList = new ArrayList();
        AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null)
            return arrayList;
        findViewByRect(arrayList, accessibilityNodeInfo, paramRect);
        accessibilityNodeInfo.recycle();
        return arrayList;
    }

    public static void findViewByRect(List<AccessibilityNodeInfo> paramList, AccessibilityNodeInfo paramAccessibilityNodeInfo, Rect paramRect) {
        if (paramAccessibilityNodeInfo == null)
            return;
        for (int i = 0; i < paramAccessibilityNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
            if (accessibilityNodeInfo != null) {
                accessibilityNodeInfo.getBoundsInScreen(mRecycleRect);
                if (mRecycleRect.contains(paramRect)) {
                    paramList.add(accessibilityNodeInfo);
                } else {
                    findViewByRect(paramList, accessibilityNodeInfo, paramRect);
                    accessibilityNodeInfo.recycle();
                }
            }
        }
    }

    public static int getChildCount(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (paramAccessibilityNodeInfo == null)
            return 0;
        if (paramAccessibilityNodeInfo.getClassName().equals("android.widget.FrameLayout")) {
            int i = 0;
            while (i < Integer.MAX_VALUE) {
                try {
                    AccessibilityNodeInfo accessibilityNodeInfo = paramAccessibilityNodeInfo.getChild(i);
                    if (accessibilityNodeInfo == null)
                        return i;
                    i++;
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return i;
                }
            }
            return 0;
        }
        return paramAccessibilityNodeInfo.getChildCount();
    }

    public static void recycleAccessibilityNodeInfo(List<AccessibilityNodeInfo> paramList) {
        if (Utils.isEmptyArray(paramList))
            return;
        Iterator<AccessibilityNodeInfo> iterator = paramList.iterator();
        while (iterator.hasNext())
            ((AccessibilityNodeInfo) iterator.next()).recycle();
    }

    public static void setText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, paramString);
        paramAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        paramAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void clickByGesture(AccessibilityService service, AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (service == null) return;
        Rect rect = new Rect();
        paramAccessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo(rect.left, rect.top);
        service.dispatchGesture(
                new GestureDescription.Builder().addStroke(
                        new GestureDescription.StrokeDescription(path, 100L, 50L)
                ).build(), null, null
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void clickByGesture(AccessibilityService service, int x, int y) {
        if (service == null) return;
        Path path = new Path();
        path.moveTo(x, y);
        service.dispatchGesture(
                new GestureDescription.Builder().addStroke(
                        new GestureDescription.StrokeDescription(path, 50L, 100L)
                ).build(), null, null
        );
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void doubleClickByGesture(AccessibilityService service, AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        if (service == null) return;
        Rect rect = new Rect();
        paramAccessibilityNodeInfo.getBoundsInScreen(rect);
        Path path = new Path();
        path.moveTo((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
        path.lineTo((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2 + 10);
        Log.e("----", "path " + (rect.left + rect.right) / 2);
        service.dispatchGesture(
                new GestureDescription.Builder().addStroke(
                        new GestureDescription.StrokeDescription(path, 100L, 50L)
                ).build(), null, null
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void doubleClickView(AccessService service, int paramInt1, int paramInt2) {
        final Path path = new Path();
        path.moveTo(paramInt1, paramInt2);
        service.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 0L, 100L)).build(), new AccessibilityService.GestureResultCallback() {
            public void onCompleted(GestureDescription param1GestureDescription) {
                super.onCompleted(param1GestureDescription);
                service.dispatchGesture((new GestureDescription.Builder()).addStroke(new GestureDescription.StrokeDescription(path, 150L, 100L)).build(), null, null);
            }
        }, null);
    }

    public static void sortList(List<AccessibilityNodeInfo> paramList) {
        Collections.sort(paramList, new order());
    }

    public static class order implements Comparator<AccessibilityNodeInfo> {
        public int compare(AccessibilityNodeInfo param1AccessibilityNodeInfo1, AccessibilityNodeInfo param1AccessibilityNodeInfo2) {
            Rect rect2 = new Rect();
            param1AccessibilityNodeInfo1.getBoundsInScreen(rect2);
            Rect rect1 = new Rect();
            param1AccessibilityNodeInfo2.getBoundsInScreen(rect1);
            return rect2.left + rect2.top - rect1.left + rect1.top;
        }
    }
}
