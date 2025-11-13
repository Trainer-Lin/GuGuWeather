package com.example.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location

class WeatherViewModel: ViewModel() {
    private val _weatherLiveData = MutableLiveData<Location>()
    var locationLng = 0.0



    var locationLat = 0.0

    var placeName = ""

    val weatherLiveData = _weatherLiveData.switchMap {
        location ->
        Repository.searchWeather(location.lat , location.lng)
    }

    fun searchWeather(location: Location){
        _weatherLiveData.value = location
     }

}