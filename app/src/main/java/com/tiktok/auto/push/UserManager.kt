package com.tuolu.subtitle.bean

import com.google.gson.Gson
import com.tuolu.remove.util.PreferencesUtil
import com.tiktok.auto.bean.auth.UserBean

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/22
 */
class UserManager private constructor() {
    private var mUserBean: UserBean? = null
    private var mUserId: Int = -1

    //单例模式
    companion object {
        const val KEY_USER = "key_user"
        const val KEY_USER_ID = "key_user_id"

        @JvmStatic
        val instance: UserManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserManager()
        }
    }

    fun setUser(user: UserBean?) {
        mUserBean = user
        if (user != null) {
            PreferencesUtil.saveValue(KEY_USER, Gson().toJson(user))
        } else {
            PreferencesUtil.saveValue(KEY_USER, "")
        }
    }

    fun getUser(): UserBean? {
        if (mUserBean == null) {
            mUserBean =
                Gson().fromJson(PreferencesUtil.getString(KEY_USER, ""), UserBean::class.java)
        }
        return mUserBean
    }

    fun setUserId(userId: Int) {
        mUserId = userId
        if (userId >= 0) {
            PreferencesUtil.saveValue(KEY_USER_ID, userId)
        } else {
            PreferencesUtil.saveValue(KEY_USER_ID, -1)
        }
    }

    fun getId(): Int {
        if (mUserId < 0) {
            mUserId = PreferencesUtil.getInt(KEY_USER_ID, -1)
        }
        return mUserId
    }

    fun logout() {
        setUser(null)
//        setUserId(-1)
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }

    fun isRootUser(): Boolean {
        //username "admin_kxyd_8888"    pwd  "admin_kxyd_8888"
        return getUser()?.username == "admin_kxyd_8888"
    }
}