package logic.model

import com.google.gson.annotations.SerializedName

//TODO: 根据返回的json写一个用于解析一段时间内天气的类

data class DailyResponse (
    val status: String,
    val result: DailyResult
)

data class DailyResult(
    val daily: Daily
)

data class Daily(
    val temperature: List<Temperature>,
    val skycon: List<Skycon>,
    @SerializedName("life_index") val lifeIndex: LifeIndex
)

data class Temperature(
    val max: Float,
    val min: Float
)

data class Skycon(
    val value: String,
    val date: String
)

data class LifeIndex(
    val coldRisk: List<Desc>,
    val carWashing: List<Desc>,
    val ultraviolet: List<Desc>,
    val dressing: List<Desc>
)

data class Desc(
    val desc: String
)