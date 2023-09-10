package ru.example.androidapp.presentation.room

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
import kotlinx.coroutines.launch
import ru.example.androidapp.App
import ru.example.domain.model.HotelRoom
import ru.example.domain.usecase.GetDataHotelRooms
import javax.inject.Inject

class HotelRoomsViewModel(application: Application) : ViewModel() {
    private val _uiState: MutableStateFlow<HotelRoomsUiState> =
        MutableStateFlow(HotelRoomsUiState.Success(emptyList()))
    val uiState: StateFlow<HotelRoomsUiState> = _uiState.asStateFlow()

    @Inject
    lateinit var hotelRoomsUseCase: GetDataHotelRooms

    init {
        (application as App).appComponent.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            getDataHotelRooms()
        }
    }

    suspend fun getDataHotelRooms() = coroutineScope {
        _uiState.value = HotelRoomsUiState.Loading()
        launch {
            hotelRoomsUseCase()
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    _uiState.value = HotelRoomsUiState.Error(error)
                    Log.d("myLogs", error.message.toString())
                }
                .collect { hotelRooms ->
                    launch {
//                        DataUtils.getBitmaps(types)
                        _uiState.value = HotelRoomsUiState.Success(hotelRooms)
                    }
                }
        }
    }
}

sealed class HotelRoomsUiState {
    data class Success(val hotelRooms: List<HotelRoom>) : HotelRoomsUiState()
    data class Error(val exception: Throwable) : HotelRoomsUiState()
    class Loading : HotelRoomsUiState()
}