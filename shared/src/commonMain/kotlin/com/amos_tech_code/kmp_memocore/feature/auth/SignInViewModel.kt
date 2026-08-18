package com.amos_tech_code.kmp_memocore.feature.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignInViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()


    fun onEmailUpdated(email: String) {
        _email.value = email
    }

    fun onPasswordUpdated(password: String) {
        _password.value = password
    }


    fun signIn() {

    }
}