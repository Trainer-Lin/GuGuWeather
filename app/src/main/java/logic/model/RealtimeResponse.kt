package logic.model

import com.google.gson.annotations.SerializedName

//TODO:根据查询实时天气会返回的json字符串 写一个让Gson解析的类

data class RealtimeResponse(
    val status: String,
    val result: RealtimeResult
)

data class RealtimeResult(
    val realtime: Realtime
)

data class Realtime(
    val temperature: Float,
    val skycon: String,
    @SerializedName("air_quality") val airQuality: AirQuality
)

data class Aqi(
    val chn: Float
)

data class AirQuality(
    val aqi: Aqi
)