package com.example.sunnyweather

//TODO:根据查询天气会返回的json字符串 写一个让Gson解析的类

data class WeatherResponse(
    val status: String,
    val result: Result
)

data class Result(
    val realtime: Realtime
)

data class Realtime(
    val temperature: Double,
    val skycon: String,
    val aqi: Aqi
)

data class Aqi(
    val chn: Double
)

data class AirQuality(
    val aqi: Aqi
)