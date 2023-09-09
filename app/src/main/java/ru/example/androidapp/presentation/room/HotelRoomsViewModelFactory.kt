package ru.example.androidapp.presentation.room

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HotelRoomsViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HotelRoomsViewModel::class.java)){
            return HotelRoomsViewModel(application) as T
        }
        throw IllegalArgumentException("Class not found")
    }
}