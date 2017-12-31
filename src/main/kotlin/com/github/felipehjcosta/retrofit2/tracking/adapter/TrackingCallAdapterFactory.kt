package com.github.felipehjcosta.retrofit2.tracking.adapter

import retrofit2.*
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
        return if (isCallType(returnType!!)) {
            createCallAdapter(returnType, annotations, retrofit)
        } else {
            null
        }
    }

    private fun isCallType(type: Type) = Call::class.java == getRawType(type)

    private fun createCallAdapter(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *> {
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        return TrackingCallAdapter(responseType, createTracking())
    }

    private class TrackingCallAdapter(
            private val responseType: Type,
            private val retrofitNetworkTracking: RetrofitNetworkTracking

    ) : CallAdapter<Any, Any> {
        override fun responseType(): Type = responseType

        override fun adapt(call: Call<Any>?): Any = TrackingCallDecorator(call!!, retrofitNetworkTracking)
    }

    private class TrackingCallDecorator(
            private val decoratedCall: Call<Any>,
            private val retrofitNetworkTracking: RetrofitNetworkTracking
    ) : Call<Any> by decoratedCall {

        override fun execute(): Response<Any> {
            return try {
                decoratedCall.execute().apply { retrofitNetworkTracking.onSuccess(this) }
            } catch (e: Exception) {
                retrofitNetworkTracking.onFailure(e)
                throw e
            }
        }

        override fun enqueue(callback: Callback<Any>?) {
            decoratedCall.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                    retrofitNetworkTracking.onSuccess(response as Response<*>)
                    callback!!.onResponse(call!!, response)
                }

                override fun onFailure(call: Call<Any>?, t: Throwable?) {
                    retrofitNetworkTracking.onFailure(t!!)
                    callback!!.onFailure(call!!, t)
                }
            })
        }

        override fun clone(): Call<Any> = TrackingCallDecorator(decoratedCall.clone(), retrofitNetworkTracking)
    }
}