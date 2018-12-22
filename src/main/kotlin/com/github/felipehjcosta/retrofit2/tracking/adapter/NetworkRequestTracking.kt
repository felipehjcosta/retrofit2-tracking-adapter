package com.github.felipehjcosta.retrofit2.tracking.adapter

interface NetworkRequestTracking {
    fun onRequest(request: okhttp3.Request, response: okhttp3.Response, trackingPath: String? = null)
}
