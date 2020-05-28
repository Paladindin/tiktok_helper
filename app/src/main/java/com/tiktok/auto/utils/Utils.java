package com.tiktok.auto.utils;

import java.util.List;

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/7
 */
public class Utils {
    public static boolean isEmptyArray(List paramList) {
        return (paramList == null || paramList.size() == 0);
    }
}
