package com.example.vapulustest.ui.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vapulustest.data.remote.ApiClient
import com.example.vapulustest.data.remote.ApiServices
import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.remote.models.PincodeResponse
import com.example.vapulustest.data.remote.models.Response
import com.example.vapulustest.ui.join.login.LoginViewState
import com.example.vapulustest.ui.join.pinCode.PinCodeViewState
import retrofit2.Call
import retrofit2.Callback

/**
 * Shared ViewModel for Join classes such as login and PIN-Code verification.
 * This ViewModel is used to handle some logic of login process and PIN-Code verification.
 * */
class JoinViewModel : ViewModel() {

    private val tag: String = JoinViewModel::class.java.simpleName

    private val apiClient: ApiServices by lazy { ApiClient.apiServices }

    private val _loginViewState: MutableLiveData<LoginViewState>
            by lazy { MutableLiveData<LoginViewState>() }
    val loginViewState: LiveData<LoginViewState>
        get() {
            _loginViewState.value = LoginViewState.Initial
            return _loginViewState
        }

    private val _pinCodeViewState: MutableLiveData<PinCodeViewState>
            by lazy { MutableLiveData<PinCodeViewState>() }
    val pinCodeViewState: LiveData<PinCodeViewState>
        get() {
            _pinCodeViewState.value = PinCodeViewState.Initial
            return _pinCodeViewState
        }

    private val _pinCode: MutableLiveData<String>
            by lazy { MutableLiveData<String>() }
    val pinCode: LiveData<String>
        get() {
            _pinCode.value = ""
            return _pinCode
        }

    /**
     * Perform user login
     * @param userName is Vapulus ID, email or mobile number
     * @param password is Vapulus user password
     */
    fun performLogin(userName: String, password: String) {
        _loginViewState.value = LoginViewState.Loading

        apiClient.login(userName, password)
            .enqueue(object : Callback<Response<LoginResponse>> {
                override fun onResponse(
                    call: Call<Response<LoginResponse>>,
                    response: retrofit2.Response<Response<LoginResponse>>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode in 200..204)
                        _loginViewState.value =
                            LoginViewState.LoginSuccess(response.body()?.data!!)
                    else if (response.isSuccessful.not() && response.body() != null)
                        _loginViewState.value =
                            LoginViewState.LoginErrorError(response.body()?.message.toString())
                    else
                        _loginViewState.value = LoginViewState.LoginErrorError("Unknown")
                }

                override fun onFailure(call: Call<Response<LoginResponse>>, t: Throwable) {
                    Log.e(tag, t.message.toString())
                    _loginViewState.value = LoginViewState.LoginErrorError("Unknown")
                }
            })
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
     * @return the user data that will used for PIN-Code checking.
     * */
    fun getUserCredentials(): LoginResponse {
        val userCredentials = _loginViewState.value as LoginViewState.LoginSuccess
        return userCredentials.loginData
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
     * Call the api to check the PIN-Code is valid oe not.
     * @param userToken that is returned from the login process.
     * @param deviceToken that is returned from the login process.
     */
    fun validatePinCode(userToken: String, deviceToken: String, pinCode: String) {
        _pinCodeViewState.value = PinCodeViewState.Loading

        apiClient.validatePinCode(userToken, deviceToken, pinCode)
            .enqueue(object : Callback<PincodeResponse> {
                override fun onResponse(
                    call: Call<PincodeResponse>,
                    response: retrofit2.Response<PincodeResponse>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode in 200..204)
                        _pinCodeViewState.value =
                            PinCodeViewState.PinCodeValid(response.body()?.message.toString())
                    else
                        _pinCodeViewState.value =
                            PinCodeViewState.PinCodeError(response.body()?.message.toString())
                }

                override fun onFailure(call: Call<PincodeResponse>, t: Throwable) {
                    Log.e(tag, "PinCode Error: ${t.message}")
                    _pinCodeViewState.value = PinCodeViewState.PinCodeError("Unknown")
                }
            })
    }
}