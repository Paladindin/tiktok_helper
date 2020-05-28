package com.tiktok.auto.config

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.tuolu.remove.util.PreferencesUtil
import com.tiktok.auto.bean.control.ControlConfigBean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/2
 */
class ConfigManager private constructor() {

    //单例模式
    companion object {
        const val KEY_GREET_WORD = "key_greet_word"
        const val KEY_BATCH_USER = "key_batch_user"
        const val KEY_TRAIN_CONFIG = "key_train_config"
        const val KEY_DATA_CONFIG = "key_data_config"
        const val KEY_CONTROL_CONFIG = "key_control_config"
        @JvmStatic
        val instance: ConfigManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ConfigManager()
        }
    }

    fun getGreetWordSize(): Int {
        return getGreetWordList().size
    }

    fun getGreetWordList(): List<String> {
        val greetWordList = ArrayList<String>()
        getGreetWordStr().takeIf { it.isNotEmpty() }?.let {
            greetWordList.addAll(it.split("%"))
        }
        return greetWordList
    }

    fun getGreetWordStr() = PreferencesUtil.getString(KEY_GREET_WORD)

    fun saveGreetWordStr(greetWordStr: String) {
        PreferencesUtil.saveValue(KEY_GREET_WORD, greetWordStr)
    }

    fun getGreetWord(): String {
//        var isRandom = getDataConfig().greetWordRandom
        val commentConfig = getControlConfig().commentConfig
        var isRandom = commentConfig.isRandom
        var greetWord = "你好"
//        val greetWordList = getGreetWordList()
        val greetWordList = commentConfig.commentContentList
        if (!greetWordList.isEmpty()) {
            if (isRandom) {
                greetWord = greetWordList.random()
            } else {
                greetWord = greetWordList[0]
            }
        }
        return greetWord
    }

    fun getCommentInterval(): Int {
        val commentConfig = getControlConfig().commentConfig
        return commentConfig.commentIntervalTime
    }

    fun getBatchUserSize(): Int {
        return getBatchUserList().size
    }

    fun getBatchUserList(): List<String> {
        val batchUserList = ArrayList<String>()

        getBatchUserStr().takeIf { it.isNotEmpty() }?.let {
            batchUserList.addAll(it.split("%"))
        }
        if (batchUserList.isEmpty()){
            batchUserList.add("美食")
        }
        return batchUserList
    }

    fun getBatchUserStr() = PreferencesUtil.getString(KEY_BATCH_USER)

    fun saveBatchUserStr(batchUserStr: String) {
        PreferencesUtil.saveValue(KEY_BATCH_USER, batchUserStr)
    }

    fun getTrainConfig(): TrainConfig {
        val trainConfigStr = PreferencesUtil.getString(KEY_TRAIN_CONFIG)
        if (!TextUtils.isEmpty(trainConfigStr)) {
            return Gson().fromJson(trainConfigStr, TrainConfig::class.java)
        } else {
            return TrainConfig()
        }

    }

    fun saveTrainConfig(trainConfig: TrainConfig) {
        Gson().toJson(trainConfig)?.let {
            PreferencesUtil.saveValue(KEY_TRAIN_CONFIG, it)
        }
    }

    fun saveDataConfig(dataConfig: DataConfig) {
        Gson().toJson(dataConfig)?.let {
            PreferencesUtil.saveValue(KEY_DATA_CONFIG, it)
        }
    }

    fun getDataConfig(): DataConfig {
        val trainConfigStr = PreferencesUtil.getString(KEY_DATA_CONFIG)
        if (!TextUtils.isEmpty(trainConfigStr)) {
            return Gson().fromJson(trainConfigStr, DataConfig::class.java)
        } else {
            return DataConfig()
        }
    }

    fun saveControlConfig(controlConfigBean: ControlConfigBean) {
        Gson().toJson(controlConfigBean)?.let {
            Log.e("------","controlConfigBean $it")
            PreferencesUtil.saveValue(KEY_CONTROL_CONFIG, it)
        }
    }

    fun getControlConfig(): ControlConfigBean {
        val trainConfigStr = PreferencesUtil.getString(KEY_CONTROL_CONFIG)
        if (!TextUtils.isEmpty(trainConfigStr)) {
            return Gson().fromJson(trainConfigStr, ControlConfigBean::class.java)
        } else {
            return ControlConfigBean()
        }
    }
}