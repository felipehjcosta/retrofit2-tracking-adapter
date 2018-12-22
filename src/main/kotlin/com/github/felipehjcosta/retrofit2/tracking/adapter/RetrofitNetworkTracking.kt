package com.github.felipehjcosta.retrofit2.tracking.adapter

import retrofit2.Response

interface RetrofitNetworkTracking {
    fun onSuccess(response: Response<*>, trackingPath: String? = null)

    fun onFailure(throwable: Throwable, trackingPath: String? = null)
}
