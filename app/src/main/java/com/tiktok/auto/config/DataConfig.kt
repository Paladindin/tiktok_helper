package com.tiktok.auto.config

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/2
 */
data class DataConfig(
    var operateCount: Int = 0,
    var meaageInterval: Int = 1000,
    var perBatchCount: Int = 0,
    var perLoopSleepTime: Int = 1000,
    var perTimeGreetWordCount: Int = 0,
    var greetWordRandom: Boolean = false,
    var operateSex: Sex? = null
){
    enum class Sex{
        MALE,FEMAL,UNKONWN
    }
}