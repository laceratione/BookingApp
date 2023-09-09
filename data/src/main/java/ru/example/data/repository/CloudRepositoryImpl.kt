package ru.example.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.example.data.api.RetrofitAPI
import ru.example.data.model.mapToDomain
import ru.example.domain.model.BookingData
import ru.example.domain.model.Hotel
import ru.example.domain.model.HotelRoom
import ru.example.domain.repository.CloudRepository

class CloudRepositoryImpl(private val retrofitAPI: RetrofitAPI) : CloudRepository {
    override fun getHotel(): Flow<Hotel> = flow {
        emit(retrofitAPI.getHotel().mapToDomain())
    }

    override fun getHotelRooms(): Flow<List<HotelRoom>> = flow {
        emit(retrofitAPI.getHotelRooms().mapToDomain())
    }

    override fun getBookingData(): Flow<BookingData> = flow{
        emit(retrofitAPI.getBookingData().mapToDomain())
    }
}