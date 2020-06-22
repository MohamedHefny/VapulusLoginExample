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

    private val _loginResponse: MutableLiveData<LoginResponse> by lazy { MutableLiveData<LoginResponse>() }
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    private val _loginErrorMsg: MutableLiveData<String?> by lazy { MutableLiveData<String?>() }
    val loginErrorMsg: LiveData<String?>
        get() = _loginErrorMsg

    private val _pinCode: MutableLiveData<String>
            by lazy { MutableLiveData<String>() }
    val pinCode: LiveData<String>
        get() {
            _pinCode.value = ""
            return _pinCode
        }

    private val _pinCodeViewState: MutableLiveData<PinCodeViewState>
            by lazy { MutableLiveData<PinCodeViewState>() }
    val pinCodeViewState: LiveData<PinCodeViewState>
        get() {
            _pinCodeViewState.value = PinCodeViewState.Initial
            return _pinCodeViewState
        }

    /**
     * Perform user login
     * @param userName is Vapulus ID, email or mobile number
     * @param password is Vapulus user password
     */
    fun performLogin(userName: String, password: String) {
        apiClient.login(userName, password)
            .enqueue(object : Callback<Response<LoginResponse>> {
                override fun onResponse(
                    call: Call<Response<LoginResponse>>,
                    response: retrofit2.Response<Response<LoginResponse>>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode in 200..204)
                        _loginResponse.value = response.body()?.data
                    else if (response.body() != null)
                        _loginErrorMsg.value = response.body()?.message
                    else
                        _loginErrorMsg.value = null
                }

                override fun onFailure(call: Call<Response<LoginResponse>>, t: Throwable) {
                    Log.e(tag, t.message.toString())
                    _loginErrorMsg.value = null
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

    fun validatePinCode(
        userToken: String = loginResponse.value!!.userToken,
        deviceToken: String = loginResponse.value!!.deviceToken,
        pinCode: String
    ) {
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