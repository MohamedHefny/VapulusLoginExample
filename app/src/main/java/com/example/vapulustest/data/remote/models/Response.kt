package com.example.vapulustest.data.remote.models

data class Response<T>(val statusCode: Int, val message: String, val data: T? = null)