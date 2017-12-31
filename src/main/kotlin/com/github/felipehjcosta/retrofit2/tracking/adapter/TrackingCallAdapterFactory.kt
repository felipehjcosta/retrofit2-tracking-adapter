package com.github.felipehjcosta.retrofit2.tracking.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class TrackingCallAdapterFactory private constructor(
        private val createTracking: () -> RetrofitNetworkTracking
) : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(createTracking: () -> RetrofitNetworkTracking) = TrackingCallAdapterFactory(createTracking)
    }

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {

        if (Call::class.java != getRawType(returnType!!)) {
            return null
        }

        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)

        return object : CallAdapter<Any, Any> {

            override fun responseType(): Type? = responseType

            override fun adapt(call: Call<Any>?): Any = TrackingCallDecorator(call!!, createTracking)
        }
    }

    private class TrackingCallDecorator(
            private val decoratedCall: Call<Any>,
            private val createTracking: () -> RetrofitNetworkTracking
    ) : Call<Any> by decoratedCall {

        override fun execute(): Response<Any> = try {
            decoratedCall.execute().apply { createTracking().onSuccess(this) }
        } catch (e: Exception) {
            createTracking().onFailure(e)
            throw e
        }
    }
}