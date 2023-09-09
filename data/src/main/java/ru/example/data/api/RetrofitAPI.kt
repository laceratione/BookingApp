package ru.example.data.api

import retrofit2.http.GET
import ru.example.data.model.BookingDataDTO
import ru.example.data.model.HotelDTO
import ru.example.data.model.HotelRoomsDTO

interface RetrofitAPI {
    @GET("/v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3")
    suspend fun getHotel(): HotelDTO

    @GET("/v3/f9a38183-6f95-43aa-853a-9c83cbb05ecd")
    suspend fun getHotelRooms(): HotelRoomsDTO

    @GET("/v3/e8868481-743f-4eb2-a0d7-2bc4012275c8")
    suspend fun getBookingData(): BookingDataDTO
}