package com.github.felipehjcosta.retrofit2.tracking.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TrackingCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = TrackingCallAdapterFactory()
    }

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        return object :CallAdapter<Nothing, Nothing> {

            override fun responseType(): Type {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun adapt(call: Call<Nothing>?): Nothing {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}