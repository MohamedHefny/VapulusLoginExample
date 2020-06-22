package com.example.vapulustest.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.repositories.JoinRepository
import com.example.vapulustest.ui.join.login.LoginViewState
import com.example.vapulustest.ui.join.pinCode.PinCodeViewState

/**
 * Shared ViewModel for Join classes such as login and PIN-Code verification.
 * This ViewModel is used to handle some logic of login process and PIN-Code verification.
 * */
class JoinViewModel : ViewModel() {

    //This object is hold the user credentials data that needed to validate the PIB-Code
    var userCredentials: LoginResponse? = null
        private set

    private val _pinCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pinCode: LiveData<String>
        get() {
            _pinCode.value = ""
            return _pinCode
        }

    /**
     * Validate the data used for login process.
     * @param userName is Vapulus ID, email or mobile number
     * @param password is Vapulus user password
     *
     * Note: The valid checking criteria should be provided before production time.
     */
    fun isLoginDataValid(userName: String, password: String): Boolean {
        return userName.isNotEmpty() && userName.length > 2
                && password.isNotEmpty() && password.length > 2
    }

    /**
     * Add new PIN-number to the existing PIN-Code
     * @param pinNumber is the next new number of the PIN-Code.
     */
    fun addToPinCode(pinNumber: String) {
        _pinCode.value = _pinCode.value?.plus(pinNumber)
    }

    /**
     * Reset the PIN-Code
     * */
    fun resetPinCode() {
        _pinCode.value = ""
    }

    /**
     * Perform user login
     * @param userName is Vapulus ID, email or mobile number
     * @param password is Vapulus user password
     * @return a LiveData object that holds the view state.
     */
    fun performLogin(userName: String, password: String): LiveData<LoginViewState> =
        Transformations.map(JoinRepository.performLogin(userName, password)) {
            if (it is LoginViewState.LoginSuccess)
                userCredentials = it.loginData
            it
        }

    /**
     * Call the api to check the PIN-Code is valid oe not.
     * @param userToken that is returned from the login process.
     * @param deviceToken that is returned from the login process.
     */
    fun validatePinCode(
        userToken: String = userCredentials!!.userToken,
        deviceToken: String = userCredentials!!.deviceToken,
        pinCode: String
    ): LiveData<PinCodeViewState> = Transformations
        .map(JoinRepository.validatePinCode(userToken, deviceToken, pinCode)) {
            it
        }

}