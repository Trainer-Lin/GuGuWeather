package ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import logic.Repository
import logic.model.Place

/*TODO: 写一个viewModel类 管理UI的数据 为UI和Repository建立联系
        1.定义一个内部私有的可变liveData 并且用内部方法实时更新他的值 用于监视数据变化并从仓库获取数据
        2.定义一个暴露给外部的liveData 用于用switchMap给UI层提供数据变化
        3.为了UI层定义一个searchPlace来让他观察的一直是最新的LiveData， 定义一个placeList用来给UI层注入数据
 */
class PlaceViewModel: ViewModel() {
    private val _placeLiveData = MutableLiveData<String>() //内部私有可变变量
    val placeList = ArrayList<Place>() //给UI层用的
    val placeLiveData = _placeLiveData.switchMap {
        query -> Repository.searchPlace(query)
    }//监视数据变化

    fun searchPlace(query: String){
        _placeLiveData.value = query
    }//给UI层用的

}