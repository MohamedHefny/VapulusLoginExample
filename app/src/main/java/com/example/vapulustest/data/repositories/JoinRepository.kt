package com.example.vapulustest.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vapulustest.data.remote.ApiClient
import com.example.vapulustest.data.remote.ApiServices
import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.remote.models.Response
import com.example.vapulustest.ui.join.login.LoginViewState
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
}