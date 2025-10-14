package logic.network

import logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//https://api.caiyunapp.com/v2.6/place?query=北京&token={token}&lang=zh_CN
interface PlaceService {
    @GET("place")
    fun getPlaces(
        @Query("query") query:String,
        @Query("token") token:String,
        @Query("lang")lang:String//使用Query注解可以在拼接时自动拼上去 比硬编码实用
    ): Call<PlaceResponse>  //Call类型是一个 请求类型 是一个对网络发起请求的具体对象
}