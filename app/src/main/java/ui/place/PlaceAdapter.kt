package ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.SunnyWeatherApplication.Companion.context
import logic.model.Place
import logic.model.Weather
import org.w3c.dom.Text

//TODO: 标准的实现Adapter 我懒得写注释了

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("FUCK" , "onCreateViewHolderSuccess")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item , parent , false)

        val holder = ViewHolder(itemView)
        holder.itemView.setOnClickListener {
            val intent = Intent(parent.context, Weather::class.java).apply{
                val position = holder.adapterPosition
                val place = placeList[position]
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.startActivity(intent)
            fragment.activity?.finish()
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