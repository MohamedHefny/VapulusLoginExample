package com.example.vapulustest.ui.join.pinCode

sealed class PinCodeViewState {
     object Loading : PinCodeViewState()
    data class PinCodeValid(val message: String) : PinCodeViewState()
    data class PinCodeError(val message: String) : PinCodeViewState()
}