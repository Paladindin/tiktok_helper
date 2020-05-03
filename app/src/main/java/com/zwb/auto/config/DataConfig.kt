package com.zwb.auto.config

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/2
 */
data class DataConfig(
    var operateCount: Int,
    var meaageInterval: Int,
    var perBatchCount: Int,
    var perLoopSleepTime: Int,
    var perTimeGreetWordCount: Int,
    var greetWordRandom: Boolean = false,
    var operateSex: Sex = Sex.UNKONWN
){
    enum class Sex{
        MALE,FEMAL,UNKONWN
    }
}