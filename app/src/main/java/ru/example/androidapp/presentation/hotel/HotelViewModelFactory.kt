package ru.example.androidapp.presentation.hotel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HotelViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HotelViewModel::class.java)){
            return HotelViewModel(application) as T
        }
        throw IllegalArgumentException("Class not found")
    }
}