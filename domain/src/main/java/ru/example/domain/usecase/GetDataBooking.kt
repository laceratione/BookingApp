package ru.example.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.example.domain.model.BookingData
import ru.example.domain.repository.CloudRepository

class GetDataBooking(
    private val cloudRepository: CloudRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke():
            Flow<BookingData> = withContext(defaultDispatcher) {
        val items = cloudRepository.getBookingData()
        items
    }
}