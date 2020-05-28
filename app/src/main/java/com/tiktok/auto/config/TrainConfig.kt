package com.tiktok.auto.config

/**
 * Description:
 * Author: zwb
 * Date: 2020/5/2
 */
data class TrainConfig(
    var operateCount: Int = 0,
    var operateInterval: Int = 1000,
    var commentCount: Int = 0,
    var pariseCount: Int = 0,
    var starCount: Int = 0,
    var deliveryCount: Int = 0,
    var customDeliveryWord: String = "有爱转发"
)