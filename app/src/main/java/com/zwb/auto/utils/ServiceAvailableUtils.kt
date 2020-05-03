package com.zwb.auto.utils

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import androidx.core.content.ContextCompat.startActivity
import com.zwb.auto.ui.service.AccessService

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/5
 */
object ServiceAvailableUtils {

    fun isServiceAvailable(
        mContext: Context,
        clazz: Class<out AccessibilityService>
    ): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.getPackageName() + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

     fun checkService(context: Context) {
//        if (!isServiceAvailable(context, AccessService::class.java)) {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//        }
    }
}