package ru.example.androidapp.di

import dagger.Module
import dagger.Provides
import ru.example.data.api.RetrofitAPI
import ru.example.data.repository.CloudRepositoryImpl
import ru.example.domain.repository.CloudRepository
import javax.inject.Singleton

@Module
class RepositoryModule{
    @Singleton
    @Provides
    fun provideCloudRepositoryImpl(retrofitAPI: RetrofitAPI): CloudRepository {
        return CloudRepositoryImpl(retrofitAPI)
    }
}