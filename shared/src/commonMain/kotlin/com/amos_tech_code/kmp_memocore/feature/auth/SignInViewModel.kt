package com.amos_tech_code.kmp_memocore.feature.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.remote.ApiService
import com.amos_tech_code.kmp_memocore.data.remote.HttpClientFactory
import com.amos_tech_code.kmp_memocore.model.AuthRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val apiService = ApiService(HttpClientFactory.getHttpClient())
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<AuthNavigation>()
    val navigationFlow = _navigationFlow.asSharedFlow()


    fun onErrorClick() {
        viewModelScope.launch {
            _uiState.value = AuthState.Idle
        }
    }

    fun onSuccessClick(email: String) {
        viewModelScope.launch {
            _navigationFlow.emit(AuthNavigation.NavigateToHome(email))
        }
    }

    fun onEmailUpdated(email: String) {
        _email.value = email
    }

    fun onPasswordUpdated(password: String) {
        _password.value = password
    }


    fun signIn() {
        viewModelScope.launch {
            val request = AuthRequest(email.value, password.value)

            _uiState.value = AuthState.Loading

            val result = apiService.login(request)

            if (result.isSuccess) {
                _uiState.value = AuthState.Success(result.getOrNull()!!)
                result.getOrNull()?.let {
                    dataStoreManager.storeToken(it.accessToken)
                    dataStoreManager.storeEmail(it.email)
                    dataStoreManager.storeRefreshToken(it.refreshToken)
                    dataStoreManager.storeUserId(it.userId)
                }
            } else {
                _uiState.value = AuthState.Error(result.exceptionOrNull()?.message.toString())
            }
        }
    }
}