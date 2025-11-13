package com.example.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sunnyweather.R
import com.google.android.material.card.MaterialCardView
import com.example.sunnyweather.logic.model.Location
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.ui.place.PlaceFragment
import com.example.sunnyweather.ui.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Locale

//TODO: 设计一个完整的负责接受位置数据的类, 并展示完整天气信息

class WeatherActivity : AppCompatActivity() {

    val viewModel: WeatherViewModel by viewModels()
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        makeStatusBarTransparent()
        setContentView(R.layout.activity_weather)
        //Toast.makeText(this, "布局加载成功", Toast.LENGTH_SHORT).show()

        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        //val navBtn = findViewById<Button>(R.id.navBtn)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

//        navBtn.setOnClickListener {
//            drawerLayout.openDrawer(GravityCompat.START)
//        }

        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager//获取输入法管理器
                manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)//自动隐藏 不强制隐藏
            }

        })

//        viewModel.locationLng = intent.getDoubleExtra("location_lng", 116.4073963)
//        viewModel.locationLat = intent.getDoubleExtra("location_lat", 39.9041999)
//        viewModel.placeName = intent.getStringExtra("place_name") ?: ""

//        viewModel.locationLng = 116.4073963
//        viewModel.locationLat = 39.9041999
//        viewModel.placeName = ""

        if(viewModel.locationLng == 0.0){
            viewModel.locationLng = intent.getDoubleExtra("location_lng", 116.4073963)
        }
        if (viewModel.locationLat == 0.0) {
            viewModel.locationLat = intent.getDoubleExtra("location_lat", 39.9041999)
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        } //从place那边获取信息

        swipeRefresh.setOnRefreshListener{
            swipeRefresh.isRefreshing = true
            viewModel.searchWeather(Location(viewModel.locationLat, viewModel.locationLng))
           //Toast.makeText(this, "lng is ${viewModel.locationLng}, lat is ${viewModel.locationLat}", Toast.LENGTH_SHORT).show()
        }//刷新事件


        viewModel.weatherLiveData.observe(this, Observer{
                result ->
            val weather = result.getOrNull()
            if(weather != null){
               // Toast.makeText(this , "temperature is ${weather.realtime.temperature}", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, "Daily is ${weather.daily.temperature[0]}", Toast.LENGTH_SHORT).show()
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })  //使用viewModel自动观察的模板，1.获取被观察对象 2.空或非空处理对象
        viewModel.searchWeather(Location(viewModel.locationLat, viewModel.locationLng))//根据位置信息请求天气数据
    }

    //用于展示天气信息的函数
    private fun showWeatherInfo(weather: Weather) {
        //Toast.makeText(this, "Oh,hi So you get here", Toast.LENGTH_SHORT).show()

        //先赋值，然后判空用Toast打印看哪个是空的
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
        val weatherLayout = findViewById<ScrollView>(R.id.weatherLayout) //控件们

        //currentTemp.text = "25"

        val daily = weather.daily
        val realtime = weather.realtime //反正写到后面发现复用率高了自己就会来写这个

        placeName.text = viewModel.placeName
        currentTemp.text = "${realtime.temperature}℃"
        currentSky.text = realtime.skycon
        currentAQI.text = "空气指数${realtime.airQuality.aqi.chn.toInt()}" //利用viewModel获取到的数据更新UI
//取消以下注释

        //nowLayout.setBackgroundResource(R.drawable.leavesbackground) //根据skycon设置对应的背景图
        forecastLayout.removeAllViews() //刷新未来天气信息 这一步是删除原来的信息 下面所做的就是更新数据
        val minSize = minOf(daily.skycon.size, daily.temperature.size)

        for (i in 0 until minSize) { //反正用daily的size就行了 别管daily还是temperature
            val skycon = daily.skycon[i]
            //  Toast.makeText(this, "skycon is ${skycon.value}", Toast.LENGTH_SHORT).show()
            val temp = daily.temperature[i] //获取遍历到的天气信息
            //Toast.makeText(this, "temp is ${temp.min} ~ ${temp.max}", Toast.LENGTH_SHORT).show()

            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, forecastLayout, false) //膨胀一个子视图
            //Toast.makeText(this, "还在动！", Toast.LENGTH_SHORT).show()

            val dateInfo: TextView = view.findViewById(R.id.dateInfo)
            val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
            val skyInfo: TextView = view.findViewById(R.id.skyInfo)
            val temperatureInfo: TextView =
                view.findViewById(R.id.tempInfo) //一定要用当前view的findViewById动态获取控件

            //dateInfo.text = "2025-11-12"
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.getDefault())
                val date = inputFormat.parse(skycon.date)
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateInfo.text = outputFormat.format(date)
            } catch (e: Exception) {
                // 如果解析失败，使用简单截取
                dateInfo.text = if (skycon.date != null && skycon.date.length >= 10) {
                    skycon.date.substring(0, 10)
                } else {
                    "日期未知"
                }
            }

            val sky = getSky(skycon.value) //获取一个sky对象
            skyIcon.setImageResource(sky.icon) //设置一个小图标, 就说每日预报里最左边那个
            skyInfo.text = sky.info //"晴" 等天气信息
            temperatureInfo.text = "${temp.min} ~ ${temp.max}" //温度
            forecastLayout.addView(view) //上面是在填充一个子项 这个是把子项添加到forecastLayout中
        }

        val lifeIndex = daily.lifeIndex //只用获取当前的lifeIndex 其他的没必要

        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc //lifeIndex返回的是一个包含多日数据的数组 所以这里要取第一个

        coldRiskText.setTextColor(Color.BLACK)
        dressingText.setTextColor(Color.BLACK)
        ultravioletText.setTextColor(Color.BLACK)
        carWashingText.setTextColor(Color.BLACK)

    }

    private fun makeStatusBarTransparent() {
        // 使用WindowCompat确保最佳兼容性
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 使用WindowInsetsController控制状态栏
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.let {
            // 隐藏状态栏
            it.hide(WindowInsetsCompat.Type.statusBars())
            // 设置状态栏图标为浅色（适合深色背景）
            it.isAppearanceLightStatusBars = false
        }

        // 设置状态栏颜色为透明
        window.statusBarColor = Color.TRANSPARENT
    }

}