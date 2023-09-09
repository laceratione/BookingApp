package ru.example.androidapp.di

import dagger.Component
import ru.example.androidapp.presentation.booking.BookingViewModel
import ru.example.androidapp.presentation.hotel.HotelViewModel
import ru.example.androidapp.presentation.room.HotelRoomsViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, UseCaseModule::class])
interface AppComponent {
    fun inject(hotelViewModel: HotelViewModel)
    fun inject(hotelRoomsViewModel: HotelRoomsViewModel)
    fun inject(bookingViewModel: BookingViewModel)
}