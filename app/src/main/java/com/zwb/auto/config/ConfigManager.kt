package com.zwb.auto.config

import android.text.TextUtils
import com.google.gson.Gson
import com.tuolu.remove.util.PreferencesUtil
import kotlin.random.Random

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
        var isRandom = getDataConfig().greetWordRandom
        var greetWord = "你好"
        val greetWordList = getGreetWordList()
        if (!greetWordList.isEmpty()) {
            if (isRandom) {
                greetWord = greetWordList.random()
            } else {
                greetWord = greetWordList[0]
            }
        }
        return greetWord
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

    fun saveTrainConfig(dataConfig: DataConfig) {
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

}