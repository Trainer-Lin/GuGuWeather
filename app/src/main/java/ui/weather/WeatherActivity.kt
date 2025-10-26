package ui.weather

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import com.example.sunnyweather.R
import com.google.android.material.card.MaterialCardView
import logic.model.Location
import logic.model.Weather
import logic.model.getSky
import org.w3c.dom.Text
import ui.place.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Locale

//TODO: 设计一个完整的负责接受位置数据的类, 并展示完整天气信息

class WeatherActivity : AppCompatActivity() {

    val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)

        if(viewModel.locationLat.isNotEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer{
          result ->
            val weather = result.getOrNull()
            if(weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
            viewModel.searchWeather(Location(viewModel.locationLng, viewModel.locationLat))
    }

    private fun showWeatherInfo(weather: Weather){
        val placeName = findViewById<TextView>(R.id.placeName)
        val currentTemp = findViewById<TextView>(R.id.currentTemp)
        val currentSky = findViewById<TextView>(R.id.currentSky)
        val currentAQI = findViewById<TextView>(R.id.currentAQI)
        val nowLayout = findViewById<RelativeLayout>(R.id.now)
        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
        val forecast = findViewById<MaterialCardView>(R.id.forecast)
        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
        val dressingText = findViewById<TextView>(R.id.dressingText)
        val ultravioletText = findViewById<TextView>(R.id.ultravioletText)
        val carWashingText = findViewById<TextView>(R.id.carWashingText)
        val weatherLayout = findViewById<ScrollView>(R.id.weatherLayout)

        val daily = weather.daily
        val realtime = weather.realtime

        placeName.text = viewModel.placeName
        currentTemp.text = "${realtime.temperature.toInt()}℃"
        currentSky.text =realtime.skycon
        currentAQI.text = "空气指数${realtime.airQuality.aqi.chn.toInt()}"
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        forecastLayout.removeAllViews()

        for(i in 0 until daily.skycon.size){
            val skycon = daily.skycon[i]
            val temp = daily.temperature[i]

            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecast, false)

            val dateInfo: TextView = view.findViewById(R.id.dateInfo)
            val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
            val skyInfo: TextView = view.findViewById(R.id.skyInfo)
            val temperatureInfo: TextView = view.findViewById(R.id.tempInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sky = getSky(skycon.value)
            dateInfo.text = simpleDateFormat.format(skycon.date)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            temperatureInfo.text = "${temp.min.toInt()} ~ ${temp.max.toInt()}"
            forecastLayout.addView(view)
        }
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE

    }

}