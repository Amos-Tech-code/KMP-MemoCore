package com.amos_tech_code.kmp_memocore.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import com.amos_tech_code.kmp_memocore.data.remote.ApiService
import com.amos_tech_code.kmp_memocore.data.remote.HttpClientFactory
import com.amos_tech_code.kmp_memocore.model.AuthRequest
import com.amos_tech_code.kmp_memocore.model.AuthResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val apiService: ApiService = ApiService(HttpClientFactory.getHttpClient())

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

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun signUp() {
        viewModelScope.launch {
            val request = AuthRequest(email.value, password.value)
            _uiState.value = AuthState.Loading

            apiService.signup(request).onSuccess {

                _uiState.value = AuthState.Success(it)

                dataStoreManager.storeToken(it.accessToken)
                dataStoreManager.storeEmail(it.email)
                dataStoreManager.storeRefreshToken(it.refreshToken)
                dataStoreManager.storeUserId(it.userId)

            }.onFailure {
                _uiState.value = AuthState.Error(it.message ?: "Something went wrong")
            }
        }
    }


}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}


sealed class AuthNavigation {
    data class NavigateToHome(val email: String) : AuthNavigation()

}