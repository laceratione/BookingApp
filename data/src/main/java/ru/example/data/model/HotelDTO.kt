package ru.example.data.model

import com.google.gson.annotations.SerializedName
import ru.example.domain.model.AboutHotel
import ru.example.domain.model.Hotel

data class HotelDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("adress") val adress: String,
    @SerializedName("minimal_price") val minimalPrice: Int,
    @SerializedName("price_for_it") val priceForIt: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("rating_name") val ratingName: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("about_the_hotel") val aboutTheHotel: AboutHotel
)

data class AboutHotelDTO(
    @SerializedName("description") val description: String,
    @SerializedName("peculiarities") val peculiarities: List<String>
)

fun HotelDTO.mapToDomain() = Hotel(
    id = id,
    name = name,
    adress = adress,
    minimalPrice = minimalPrice,
    priceForIt = priceForIt,
    rating = rating,
    ratingName = ratingName,
    imageUrls = imageUrls,
    aboutTheHotel = aboutTheHotel
)