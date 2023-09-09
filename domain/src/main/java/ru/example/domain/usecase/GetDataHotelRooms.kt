package ru.example.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.example.domain.model.Hotel
import ru.example.domain.model.HotelRoom
import ru.example.domain.repository.CloudRepository

class GetDataHotelRooms(
    private val cloudRepository: CloudRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke():
            Flow<List<HotelRoom>> = withContext(defaultDispatcher) {
        val items = cloudRepository.getHotelRooms()
        items
    }
}