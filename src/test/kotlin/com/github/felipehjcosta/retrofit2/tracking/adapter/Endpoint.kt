package com.github.felipehjcosta.retrofit2.tracking.adapter

import retrofit2.Call
import retrofit2.http.GET

interface Endpoint {

    @GET("/test")
    fun get(): Call<String>
}