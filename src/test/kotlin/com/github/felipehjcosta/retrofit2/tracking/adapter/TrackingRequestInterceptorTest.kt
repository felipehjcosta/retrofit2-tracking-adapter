package com.github.felipehjcosta.retrofit2.tracking.adapter

import io.mockk.mockk
import io.mockk.verify
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal class TrackingRequestInterceptorTest {

    private val expectedTrackingPath = "/test/{id}"

    private val server = MockWebServer()

    private val mockNetworkRequestTracking = mockk<NetworkRequestTracking>(relaxed = true)

    private val interceptor = TrackingRequestInterceptor { mockNetworkRequestTracking }

    private lateinit var retrofit: Retrofit

    @BeforeEach
    fun setUp() {
        server.start()
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory())
                .build()
    }

    @AfterEach
    internal fun tearDown() = server.shutdown()

    @Test
    internal fun ensureInterceptorNotifiesCallbackWithCorrectParams() {
        server.enqueue(MockResponse().setBody(""))

        val call = retrofit.create(Endpoint::class.java).get()

        call.execute()

        verify { mockNetworkRequestTracking.onRequest(any(), any(), expectedTrackingPath) }
    }
}