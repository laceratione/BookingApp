package ru.example.androidapp.presentation.hotel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import ru.example.androidapp.App
import ru.example.domain.model.Hotel
import ru.example.domain.usecase.GetDataHotel
import javax.inject.Inject

class HotelViewModel(application: Application) : ViewModel() {
    private val _uiState: MutableStateFlow<HotelUiState> =
        MutableStateFlow(HotelUiState.Success(null))
    val uiState: StateFlow<HotelUiState> = _uiState.asStateFlow()

    @Inject
    lateinit var hotelUseCase: GetDataHotel

    init {
        (application as App).appComponent.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            getDataHotel()
        }
    }

    suspend fun getDataHotel() = coroutineScope {
        _uiState.value = HotelUiState.Loading()
        launch {
            hotelUseCase()
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    _uiState.value = HotelUiState.Error(error)
                    Log.d("myLogs", error.message.toString())
                }
                .collect { hotel ->
                    launch {
                        _uiState.value = HotelUiState.Success(hotel)
                    }
                }
        }
    }
}

sealed class HotelUiState {
    data class Success(val hotel: Hotel?) : HotelUiState()
    data class Error(val exception: Throwable) : HotelUiState()
    class Loading : HotelUiState()
}