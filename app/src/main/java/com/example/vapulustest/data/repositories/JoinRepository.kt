package com.example.vapulustest.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vapulustest.data.remote.ApiClient
import com.example.vapulustest.data.remote.ApiServices
import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.remote.models.PincodeResponse
import com.example.vapulustest.data.remote.models.Response
import com.example.vapulustest.ui.join.login.LoginViewState
import com.example.vapulustest.ui.join.pinCode.PinCodeViewState
import com.example.vapulustest.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback

object JoinRepository {

    private val tag: String = JoinRepository::class.java.simpleName

    private val apiClient: ApiServices by lazy { ApiClient.apiServices }

    /**
     * Perform user login
     * @param userName is Vapulus ID, email or mobile number
     * @param password is Vapulus user password
     */
    fun performLogin(userName: String, password: String): LiveData<LoginViewState> {

        val loginViewState = MutableLiveData<LoginViewState>()
            .also { it.value = LoginViewState.Loading }

        if (NetworkUtils.isNetworkConnected().not())
            return loginViewState.also {
                it.value = LoginViewState.LoginErrorError(NetworkUtils.noInternetErrorMsg)
            }

        apiClient.login(userName, password)
            .enqueue(object : Callback<Response<LoginResponse>> {
                override fun onResponse(
                    call: Call<Response<LoginResponse>>,
                    response: retrofit2.Response<Response<LoginResponse>>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode in 200..204)
                        loginViewState.value =
                            LoginViewState.LoginSuccess(response.body()?.data!!)
                    else if (response.isSuccessful.not() && response.body() != null)
                        loginViewState.value =
                            LoginViewState.LoginErrorError(response.body()?.message.toString())
                    else
                        loginViewState.value = LoginViewState.LoginErrorError("Unknown")
                }

                override fun onFailure(call: Call<Response<LoginResponse>>, t: Throwable) {
                    Log.e(tag, t.message.toString())
                    loginViewState.value = LoginViewState.LoginErrorError("Unknown")
                }
            })
        return loginViewState
    }

    /**
     * Call the api to check the PIN-Code is valid oe not.
     * @param userToken that is returned from the login process.
     * @param deviceToken that is returned from the login process.
     */
    fun validatePinCode(
        userToken: String, deviceToken: String, pinCode: String
    ): LiveData<PinCodeViewState> {

        val pinCodeViewState = MutableLiveData<PinCodeViewState>()
            .also { it.value = PinCodeViewState.Loading }

        if (NetworkUtils.isNetworkConnected().not())
            return pinCodeViewState.also {
                it.value = PinCodeViewState.PinCodeError(NetworkUtils.noInternetErrorMsg)
            }

        apiClient.validatePinCode(userToken, deviceToken, pinCode)
            .enqueue(object : Callback<PincodeResponse> {
                override fun onResponse(
                    call: Call<PincodeResponse>,
                    response: retrofit2.Response<PincodeResponse>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode in 200..204)
                        pinCodeViewState.value =
                            PinCodeViewState.PinCodeValid(response.body()?.message.toString())
                    else
                        pinCodeViewState.value =
                            PinCodeViewState.PinCodeError(response.body()?.message.toString())
                }

                override fun onFailure(call: Call<PincodeResponse>, t: Throwable) {
                    Log.e(tag, "PinCode Error: ${t.message}")
                    pinCodeViewState.value = PinCodeViewState.PinCodeError("Unknown")
                }
            })
        return pinCodeViewState
    }
}