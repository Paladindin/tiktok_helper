package com.zwb.auto.utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Binder
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent




/**
 * Description:
 * Author: zwb
 * Date: 2020/4/12
 */
object FloatAvailableUtils {

    fun checkFloatPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                var cls = Class.forName("android.content.Context")
                val declaredField = cls.getDeclaredField("APP_OPS_SERVICE")
                declaredField.isAccessible = true
                var obj = declaredField.get(cls)
                if (obj !is String) {
                    return false
                }
                obj = cls.getMethod("getSystemService", String::class.java).invoke(context, obj)
                cls = Class.forName("android.app.AppOpsManager")
                val declaredField2 = cls.getDeclaredField("MODE_ALLOWED")
                declaredField2.isAccessible = true
                val checkOp =
                    cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String::class.java)
                val result =
                    checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName()) as Int
                return result == declaredField2.getInt(cls)
            } catch (e: Exception) {
                return false
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appOpsMgr = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                    ?: return false
                val mode = appOpsMgr.checkOpNoThrow(
                    "android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName()
                )
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED
            } else {
                return Settings.canDrawOverlays(context)
            }
        }
    }

    fun checkFloat(context: Context){
        if (Build.VERSION.SDK_INT >= 23) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            context.startActivity(intent)
        }

    }
}