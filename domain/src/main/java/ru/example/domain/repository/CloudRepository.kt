package ru.example.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.example.domain.model.BookingData
import ru.example.domain.model.Hotel
import ru.example.domain.model.HotelRoom

interface CloudRepository {
    fun getHotel(): Flow<Hotel>
    fun getHotelRooms(): Flow<List<HotelRoom>>
    fun getBookingData(): Flow<BookingData>
}