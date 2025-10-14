package logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import logic.model.Place
import logic.model.PlaceResponse
import logic.network.SunnyWeatherNetwork

//TODO: 利用LiveData 写一个纯粹的数据源

object Repository {
    fun searchPlace(query: String) = liveData(Dispatchers.IO) {
            val result = try {
                val placeResponse = SunnyWeatherNetwork.searchPlace(query)
                if (placeResponse.status == "OK") {
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
}