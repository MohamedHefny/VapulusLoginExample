package com.example.vapulustest.data.remote

import com.example.vapulustest.data.remote.models.LoginResponse
import com.example.vapulustest.data.remote.models.Response
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    @POST("/login")
    @FormUrlEncoded
    fun login(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("fingerPrint") fingerPrint: String = "FingerPrintRandomData",
        @Field("uHeader") uHeader: String = "@\"{\\\"browser\\\":{\\\"name\\\":\\\"%@\\\",\\\"version\\\":\\\"%@\\\",\\\"major\\\":\\\"0\\\"},\\\"os\\\":\\\"IOS\\\",\\\"engine\\\":\\\"mobile\\\"}\""
    ): Call<Response<LoginResponse>>

}