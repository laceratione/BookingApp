package ru.example.androidapp.di

import dagger.Module
import dagger.Provides
import ru.example.domain.repository.CloudRepository
import ru.example.domain.usecase.GetDataBooking
import ru.example.domain.usecase.GetDataHotel
import ru.example.domain.usecase.GetDataHotelRooms

@Module
class UseCaseModule {
    @Provides
    fun provideGetDataHotel(cloudRepository: CloudRepository): GetDataHotel {
        return GetDataHotel(cloudRepository)
    }

    @Provides
    fun provideGetDataHotelRooms(cloudRepository: CloudRepository): GetDataHotelRooms {
        return GetDataHotelRooms(cloudRepository)
    }

    @Provides
    fun provideGetDataBooking(cloudRepository: CloudRepository): GetDataBooking {
        return GetDataBooking(cloudRepository)
    }
}