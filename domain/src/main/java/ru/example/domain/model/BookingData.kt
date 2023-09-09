package ru.example.domain.model

data class BookingData(
    val id: Int,
    val hotelName: String,
    val hotelAdress: String,
    val horating: Int,
    val ratingName: String,

    val departure: String,
    val arrivalCountry: String,
    val tourDateStart: String,
    val tourDateStop: String,
    val numberOfNights: Int,
    val room: String,
    val nutrition: String,
    val tourPrice: Int,
    val fuelCharge: Int,
    val serviceCharge: Int
)