package com.example.sunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.ui.weather.WeatherActivity

//TODO: 标准的实现Adapter 我懒得写注释了

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }//设置子项属性 显示出来的就是名字和地址

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("FUCK" , "onCreateViewHolderSuccess")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item , parent , false)

        val holder = ViewHolder(itemView)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position] //获取点击的位置的place
            Repository.savePlace(place)//保存用户选择的城市信息

            val intent = Intent(parent.context, WeatherActivity::class.java).apply{
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }

            val activity = fragment.activity
            if(activity is WeatherActivity){
              //  Toast.makeText(activity, "成功切换到${place.name}", Toast.LENGTH_SHORT).show()
                activity.drawerLayout.closeDrawers() //如果是从天气界面的滑动窗口打开的 就关闭滑动窗口
               //目的： 直接在界面上修改ViewModel中的数据 而不是通过Intent传递
               val lng = place.location.lng
               val lat = place.location.lat
               val name = place.name
               activity.viewModel.placeName = name
               activity.viewModel.locationLng = lng
               activity.viewModel.locationLat = lat
               activity.viewModel.searchWeather(Location(lat, lng)) //调用ViewModel的searchWeather方法 刷新天气数据
            }
            else {
              //  Toast.makeText(parent.context, "点击了${place.name}", Toast.LENGTH_SHORT).show()
                fragment.startActivity(intent)
                Repository.savePlace(place)
                fragment.activity?.finish() //跳转天气界面 关闭搜索界面
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("FUCK" , "onBindViewHolderSuccess")
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}