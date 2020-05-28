package com.tiktok.auto.net

import com.tiktok.auto.bean.auth.*
import io.reactivex.Observable
import retrofit2.http.*


/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
interface AuthService {

    //注册登录
    @FormUrlEncoded
    @POST("/")
    fun signInOrUp(
        @Field("options") option: Int,
        @Field("username") username: String,
        @Field("pwd") pwd: String
    ): Observable<SignInOrUpBean>

    //注册登录
    @FormUrlEncoded
    @POST("/")
    fun modifyPwd(
        @Field("options") option: Int,
        @Field("username") username: String,
        @Field("new_pwd") pwd: String,
        @Field("manager") manager: String = "jokor_78901214"
    ): Observable<BaseAuthBean>

    //获取激活码
    @FormUrlEncoded
    @POST("/")
    fun getAuthCode(@Field("options") option: Int): Observable<AuthCodeBean>

    //激活账号
    @FormUrlEncoded
    @POST("/")
    fun activate(
        @Field("options") option: Int,
        @Field("username") username: String,
        @Field("authCode") authCode: String
    ): Observable<BaseAuthBean>

    //绑定激活账号到设备
    @FormUrlEncoded
    @POST("/")
    fun bind(
        @Field("options") option: Int,
        @Field("username") username: String,
        @Field("authCode") authCode: String,
        @Field("deviceId") deviceId: String
    ): Observable<BindBean>

    //校验激活状态
    @FormUrlEncoded
    @POST("/")
    fun checkAuthStatus(
        @Field("options") option: Int,
        @Field("deviceId") deviceId: String
    ): Observable<CheckAuthBean>

    companion object {
        const val BASE_URL = "http://117.50.37.11:9090"
    }
}