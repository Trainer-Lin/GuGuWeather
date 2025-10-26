package ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import logic.Repository
import logic.model.Location

class WeatherViewModel: ViewModel() {
    private val _weatherLiveData =  MutableLiveData<Location>()
    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData = _weatherLiveData.switchMap {
        location ->
        Repository.searchWeather(locationLng , locationLat)
    }

    fun searchWeather(location: Location){
        _weatherLiveData.value = location
     }

}