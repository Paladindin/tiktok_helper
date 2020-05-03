package com.zwb.auto.net

import com.zwb.auto.bean.BaseResponse
import com.zwb.auto.bean.ConnectBean
import com.zwb.auto.bean.IndustryBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
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