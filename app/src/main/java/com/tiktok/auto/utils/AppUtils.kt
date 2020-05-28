package com.tiktok.auto.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.provider.Settings
import android.telephony.TelephonyManager


object AppUtils {
    fun getIMEI(context: Context): String {
        try {
            //实例化TelephonyManager对象
            val telephonyManager =context.getSystemService (Context.TELEPHONY_SERVICE) as TelephonyManager
            //获取IMEI号
            var imei = telephonyManager.getDeviceId()
            if (imei == null) {
                imei = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
            }
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (e:Exception) {
            e.printStackTrace();
            return "";
        }

    }

    fun isIntentSafe(activity: Activity, uri: Uri): Boolean {
        val mapCall = Intent(Intent.ACTION_VIEW, uri)
        val packageManager = activity.packageManager
        val activities = packageManager.queryIntentActivities(mapCall, 0)
        return activities.size > 0
    }

    fun getPackageName(context: Context): String {
        try {
            val packageManager = context.packageManager;
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getVersionCode(context: Context): String {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            //返回版本号
            return packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getAppName(context: Context): String? {
        return try {
            val packageManager = context.applicationContext.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: Exception) {
            null
        }
    }


    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            val pn = (pinfo[i] as PackageInfo).packageName
            if (pn == packageName) {
                return true
            }
        }
        return false
    }

    fun jump2Wechat(context: Context) {
        val intent = Intent()
        val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = cmp
        context.startActivity(intent)
    }

}