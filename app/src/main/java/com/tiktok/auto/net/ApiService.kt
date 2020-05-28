package com.tiktok.auto.net

import com.tiktok.auto.bean.BaseResponse
import com.tiktok.auto.bean.ConnectBean
import com.tiktok.auto.bean.IndustryBean
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.QueryMap


/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
interface ApiService {

    @POST("api/category/index?project_id=100001")
    fun getIndustryList(): Observable<BaseResponse<List<IndustryBean>>>

    @POST("api/phonebook/find")
    fun getConnectList(@QueryMap map: Map<String, String>): Observable<BaseResponse<List<ConnectBean>>>

    companion object{
        const val BASE_URL = "http://dysj.xuerenwx.com"
    }
}