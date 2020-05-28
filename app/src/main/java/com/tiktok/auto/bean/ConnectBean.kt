package com.tiktok.auto.bean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
data class ConnectBean(
    var checked: Boolean = false,
    var city: String,
    var id: Int,
    var is_hot: Int,
    var level1: Int,
    var phone: String,
    var province: String,
    var type_title: String
)