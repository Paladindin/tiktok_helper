package com.tiktok.auto.bean


/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
data class BaseResponse<T>(var code: Int, var data: T, var msg: String?) {
}