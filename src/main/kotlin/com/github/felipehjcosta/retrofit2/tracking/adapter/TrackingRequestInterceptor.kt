package com.github.felipehjcosta.retrofit2.tracking.adapter

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation


class TrackingRequestInterceptor private constructor(
    private val createTrackingNetworkRequest: () -> NetworkRequestTracking
) : Interceptor {

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(createTrackingNetworkRequest: () -> NetworkRequestTracking) =
            TrackingRequestInterceptor(createTrackingNetworkRequest)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val trackingAnnotation = invocation?.method()?.getAnnotation(Tracking::class.java)
        val response = chain.proceed(request)
        createTrackingNetworkRequest().onRequest(request, response, trackingAnnotation?.path)
        return response
    }
}
