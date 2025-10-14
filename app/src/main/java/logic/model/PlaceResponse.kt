package logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    val status: String,
    val places: List<Place>
)

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String //注解作用: 这个下划线不太标准 用这个注释上 就会把这个键和address对应上
)
 data class Location(
     val lat: String,
     val lng: String
 )
