package logic.model

import com.example.sunnyweather.R

data class Sky(val info: String, val icon: Int, val bg: Int)

private val sky = mapOf(
    "CLEAR_DAY" to Sky("晴", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "CLEAR_NIGHT" to Sky("晴", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "PARTLY_CLOUDY_DAY" to Sky("多云", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "PARTLY_CLOUDY_NIGHT" to Sky("多云", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "CLOUDY" to Sky("阴", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "WIND" to Sky("大风", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "LIGHT_RAIN" to Sky("小雨", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "MODERATE_RAIN" to Sky("中雨", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "HEAVY_RAIN" to Sky("大雨", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "STORM_RAIN" to Sky("暴雨", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "THUNDER_SHOWER" to Sky("雷阵雨", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "SLEET" to Sky("雨夹雪", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "LIGHT_SNOW" to Sky("小雪", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "MODERATE_SNOW" to Sky("中雪", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "HEAVY_SNOW" to Sky("大雪", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "STORM_SNOW" to Sky("暴雪", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "HAIL" to Sky("冰雹", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "LIGHT_HAZE" to Sky("轻度雾霾", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "MODERATE_HAZE" to Sky("中度雾霾", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "HEAVY_HAZE" to Sky("重度雾霾", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "FOG" to Sky("雾", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    "DUST" to Sky("浮尘", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background)
)

fun getSky(skycon: String): Sky {
    return sky[skycon] ?: sky["CLEAR_DAY"]!!
}