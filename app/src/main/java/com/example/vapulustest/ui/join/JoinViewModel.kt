package com.example.vapulustest.ui.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vapulustest.data.remote.ApiClient
import com.example.vapulustest.data.remote.ApiServices
import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.remote.models.Response
import retrofit2.Call
import retrofit2.Callback

class JoinViewModel : ViewModel() {

    private val tag: String = JoinViewModel::class.java.simpleName

    private val apiClient: ApiServices by lazy { ApiClient.apiServices }

    private val _loginResponse: MutableLiveData<LoginResponse> by lazy { MutableLiveData<LoginResponse>() }
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    private val _loginErrorMsg: MutableLiveData<String?> by lazy { MutableLiveData<String?>() }
    val loginErrorMsg: LiveData<String?>
        get() = _loginErrorMsg

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
}