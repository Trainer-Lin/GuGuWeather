package logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import logic.model.DailyResponse
import logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Realtime: https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/realtime.json
//Daily: https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/daily.json

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng:String,
        @Path("lat") lat:String
    ):Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>

}