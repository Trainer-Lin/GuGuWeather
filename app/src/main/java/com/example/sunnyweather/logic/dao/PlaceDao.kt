package com.example.sunnyweather.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.DeprecatedSinceApi
import com.example.sunnyweather.SunnyWeatherApplication
import com.google.gson.Gson
import com.example.sunnyweather.logic.model.Place
import androidx.core.content.edit

//目的： 利用SharedPreferences为用户设计一个PlaceDao类，用于保存用户选择的城市信息, 最后在仓库层调用
//功能： 1. 保存用户选择的城市信息 2. 获取用户选择的城市信息
//Dao目的： 定义操作接口，封装增删改查逻辑

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }//保存用户选择的城市信息

    fun getPlace(): Place? {
        val placeJson = sharedPreferences().getString("place", null)
        return if (placeJson != null) Gson().fromJson(placeJson, Place::class.java) else null
    }

    fun isSavedPlace() = sharedPreferences().contains("place") //检查sharedPreference是否包含place这个键，搜索历史记录

    private fun sharedPreferences(): SharedPreferences = SunnyWeatherApplication.context.getSharedPreferences(
            "sunny_weather",
            Context.MODE_PRIVATE
        )  //一个简化的方法，用于获取SharedPreferences实例

}