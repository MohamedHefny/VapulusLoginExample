package com.example.vapulustest.ui.join.login

import com.example.vapulustest.data.remote.models.LoginResponse

sealed class LoginViewState {
    object Loading : LoginViewState()
    data class LoginSuccess(val loginData: LoginResponse) : LoginViewState()
    data class LoginError(val message: String) : LoginViewState()
}