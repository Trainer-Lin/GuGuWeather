package logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import logic.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
  TODO: 写一个统一的从网络获取数据的类 , 最终目的
        1.用ServiceCreator类创建一个内部持有的placeService类
        2. 实现异步操作逻辑 (挂起 / 暂停函数 等到continuation出现时 再继续工作) (等外卖的时候做别的事 外卖到了再拿外卖就是异步)
            具体就是去实现获取天气的数据 写失败和成功逻辑
        3.实现方式: 为网络请求对象Call<T>写一个await暂停函数 用suspendCoroutine赋予其暂停和恢复的能力 然后写获取到continuation后Callback的逻辑
                   这一步目的是把Call<T>这种异步代码转化成 await函数的协程代码 最终从网络获得body值
        4.然后就可以写searchPlace方法了
 */

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlace(query: String) = placeService.getPlaces(query).await()

    suspend fun searchRealtimeWeather(lng: String, lat: String) = weatherService.getRealtimeWeather(lng,lat).await()

    suspend fun searchDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine {
            continuation ->
            enqueue(
                object: Callback<T>{
                    override fun onResponse(p0: Call<T>, p1: Response<T>) {
                        val body = p1.body() //返回信息的正文
                        if(body != null) continuation.resume(body) //resume就是恢复进程
                        else continuation.resumeWithException(RuntimeException("response body is null"))
                    }

                    override fun onFailure(p0: Call<T>, p1: Throwable) {
                        continuation.resumeWithException(p1)
                    }
                }
            )
        }//最终以从网络获得的Callback的body作为返回值

    }



}