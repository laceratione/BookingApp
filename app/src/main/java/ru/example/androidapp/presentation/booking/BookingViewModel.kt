package ru.example.androidapp.presentation.booking

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import ru.example.androidapp.R
import ru.example.domain.model.BookingData
import ru.example.domain.usecase.GetDataBooking
import javax.inject.Inject

class BookingViewModel(application: Application) : ViewModel() {
    private val _uiState: MutableStateFlow<BookingUiState> =
        MutableStateFlow(BookingUiState.Success(null))
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    private val _loginError: MutableLiveData<LoginError> = MutableLiveData()
    val loginError: LiveData<LoginError> = _loginError

    private val _emailError: MutableLiveData<EmailError> = MutableLiveData()
    val emailError: LiveData<EmailError> = _emailError

    val countWord: List<String> =
        application.resources.getStringArray(R.array.tourists).toList()

    private var _login: String = ""

    @Inject
    lateinit var bookingUseCase: GetDataBooking

    init {
        (application as App).appComponent.inject(this)

        viewModelScope.launch {
            getBookingData()
        }
    }

    suspend fun getBookingData() = coroutineScope {
        _uiState.value = BookingUiState.Loading()
        bookingUseCase()
            .flowOn(Dispatchers.IO)
            .catch { error ->
                _uiState.value = BookingUiState.Error(error)
                Log.d("myLogs", error.message.toString())
            }
            .collect { bookingData ->
                _uiState.value = BookingUiState.Success(bookingData)
            }
    }

    fun setLogin(login: String) {
        _login = login
    }

    fun validateForm(email: String): Boolean {
        val loginValid = when {
            _login.isEmpty() -> {
                _loginError.value = LoginError.EMPTY
                false
            }
            _login.length != 10 -> {
                _loginError.value = LoginError.NOT_VALID
                false
            }
            else -> {
                _loginError.value = LoginError.VALID
                true
            }
        }

        val emailValid = when {
            email.isEmpty() -> {
                _emailError.value = EmailError.EMPTY
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _emailError.value = EmailError.NOT_VALID
                false
            }
            else -> {
                _emailError.value = EmailError.VALID
                true
            }
        }

        return loginValid && emailValid
    }
}

sealed class BookingUiState {
    data class Success(val data: BookingData?) : BookingUiState()
    data class Error(val exception: Throwable) : BookingUiState()
    class Loading : BookingUiState()
}

enum class LoginError {
    EMPTY,
    NOT_VALID,
    VALID
}

enum class EmailError {
    EMPTY,
    NOT_VALID,
    VALID
}