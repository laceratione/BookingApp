package ru.example.data.model

import com.google.gson.annotations.SerializedName
import ru.example.domain.model.HotelRoom

class HotelRoomDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("price_per") val pricePer: String,
    @SerializedName("peculiarities") val peculiarities: List<String>,
    @SerializedName("image_urls") val imageUrls: List<String>
)

fun HotelRoomDTO.mapToDomain() = HotelRoom(
    id, name, price, pricePer, peculiarities, imageUrls
)
