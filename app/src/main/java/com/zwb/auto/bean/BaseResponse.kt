package com.zwb.auto.bean

import android.icu.lang.UCharacter.GraphemeClusterBreak.T


/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
data class BaseResponse<T>(var code: Int, var data: T, var msg: String?) {
}