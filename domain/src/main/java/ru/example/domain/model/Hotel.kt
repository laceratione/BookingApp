package ru.example.domain.model

data class Hotel(
    val id: Int,
    val name: String,
    val adress: String,
    val minimalPrice: Int,
    val priceForIt: String,
    val rating: Int,
    val ratingName: String,
    val imageUrls: List<String>,
    val aboutTheHotel: AboutHotel
)

data class AboutHotel(
    val description: String,
    val peculiarities: List<String>
)