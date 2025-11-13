package com.example.sunnyweather.logic

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.sunnyweather.SunnyWeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.ServiceCreator
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import com.example.sunnyweather.logic.network.WeatherService
import kotlin.coroutines.CoroutineContext

//TODO: 利用LiveData 写一个纯粹的数据源

object Repository {

    fun getPlace() = PlaceDao.getPlace()
    fun isSavedPlace() = PlaceDao.isSavedPlace()
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun searchPlace(query: String) = liveData(Dispatchers.IO) {
            val result = try {
                val placeResponse = SunnyWeatherNetwork.searchPlace(query)
                Log.d("Net_work", "Query is $query")
                //Toast.makeText(SunnyWeatherApplication.context , "Query is $query", Toast.LENGTH_SHORT).show()
                if (placeResponse.status == "ok") {
                    Log.d("Net_work","status is ${placeResponse.status}")
                    val places = placeResponse.places
                    Result.success(places) //将result打包好
                } else {
                    val exception = RuntimeException("response status is {${placeResponse.status}}")
                    Result.failure(exception)
                }
            } catch (e: Exception) {
                Result.failure<List<Place>>(e) //这里不写e.printStackTrace()这个一般是拿来调试的 这里是要真的把错误给传给其他层
            }                                  //泛型指定的部分: 告诉拿到这个错误的东西 我要是没错本来是什么类 不然会被泛型擦除
            emit(result)// 将数据发射到UI层和ViewModel层 它决定了返回的LiveData的泛型部分的类型(liveData<Result<List<Place>>>)
        }

    fun searchWeather(lat: Double, lng: Double): LiveData<Result<Weather>>{ //可以用语法糖优化 但是用return我更喜欢

        val url = "${ServiceCreator.BASE_URL}v2.5/${SunnyWeatherApplication.TOKEN}/$lng,$lat/realtime.json"
        //Toast.makeText(SunnyWeatherApplication.context, "完整天气URL: $url", Toast.LENGTH_LONG).show()

        return fire(Dispatchers.IO){  //调用fire函数 将异步操作封装成liveData
            coroutineScope{ //coroutineScope： 保证这部分代码执行完了再继续往下走
                //async函数： 在协程作用域中并发执行函数（同时执行）
                //deferred：翻译为 “延迟 ”
                val deferredDaily = async{SunnyWeatherNetwork.searchDailyWeather(lng, lat)}
                val deferredRealtime = async{ SunnyWeatherNetwork.searchRealtimeWeather(lng, lat)}

                //await：挂起当前协程， 直到异步操作结束， 然后获取结果
                val dailyWeather = deferredDaily.await()
                val realtimeWeather = deferredRealtime.await()

                if(dailyWeather.status == "ok" && realtimeWeather.status == "ok"){
                    val realtime = realtimeWeather.result.realtime
                    val daily = dailyWeather.result.daily
                    val weather = Weather(realtime , daily)
                    Result.success(weather)
                }
                else{
                    Result.failure(RuntimeException("dailyResponse is ${dailyWeather.status} , realtimeResponse is ${realtimeWeather.status}"))
                }
            }
        }
    }

    //当发现代码重复度很高的时候 自然就会去写这个函数了
    private fun <T> fire(context: CoroutineContext, block: suspend() -> Result<T>) =
        liveData<Result<T>>(context){
          val result =  try{
                block()
            }catch(e: Exception){
                Result.failure<T>(e)
            }
            emit(result)
        } //这是一个任何需要将异步操作封装成liveData的入口函数 （封装器）

}