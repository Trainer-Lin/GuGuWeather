package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealtimeResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//Realtime: https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/realtime.json
//Daily: https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/daily.json

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng:Double,
        @Path("lat") lat: Double
    ):Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: Double,
        @Path("lat") lat: Double
    ): Call<DailyResponse>

}