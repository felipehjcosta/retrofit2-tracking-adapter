package com.github.felipehjcosta.retrofit2.tracking.adapter

import com.google.common.reflect.TypeToken
import io.mockk.mockk
import io.mockk.verify
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.MILLISECONDS


class TrackingCallAdapterFactoryTest {

    companion object {
        private val NO_ANNOTATIONS = emptyArray<Annotation>()

        private const val TIMEOUT_OF_ASYNC_TESTS_IN_MILLIS = 2_000L
    }

    private val expectedTrackingPath = "/test/{id}"

    private val server = MockWebServer()

    private val mockRetrofitNetworkTracking = mockk<RetrofitNetworkTracking>(relaxed = true)

    private val factory = TrackingCallAdapterFactory { mockRetrofitNetworkTracking }

    private lateinit var retrofit: Retrofit

    @BeforeEach
    internal fun setUp() {
        server.start()
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(500, MILLISECONDS)
                .connectTimeout(500, MILLISECONDS)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory())
                .addCallAdapterFactory(factory)
                .build()
    }

    @AfterEach
    internal fun tearDown() = server.shutdown()

    @Test
    internal fun whenCallGetWithRawTypeItShouldReturnNull() {
        val adapter = factory.get(String::class.java, NO_ANNOTATIONS, retrofit)
        assertNull(adapter)
    }

    @Test
    internal fun whenCallGetWithParametrizedTypeItShouldReturnNotNull() {
        val bodyType = typeOf<Call<String>>()
        val adapter = factory.get(bodyType, NO_ANNOTATIONS, retrofit)!!
        assertEquals(typeOf<String>(), adapter.responseType())
    }

    @Test
    internal fun whenMakeRequestWithSuccessItShouldTrackSuccess() {
        server.enqueue(MockResponse().setBody(""))

        val call = retrofit.create(Endpoint::class.java).get()

        val expectedResponse = call.execute()

        verify { mockRetrofitNetworkTracking.onSuccess(expectedResponse, expectedTrackingPath) }
    }

    @Test
    internal fun whenMakeRequestWithErrorItShouldTrackFailure() {
        val call = retrofit.create(Endpoint::class.java).get()

        val expectedThrowable = assertThrows(Exception::class.java) { call.execute() }

        verify { mockRetrofitNetworkTracking.onFailure(expectedThrowable, expectedTrackingPath) }
    }

    @Test
    internal fun whenEnqueueRequestWithSuccessItShouldTrackSuccess() {
        server.enqueue(MockResponse().setBody(""))

        val call = retrofit.create(Endpoint::class.java).get()

        var expectedResponse: Response<*>? = null
        val semaphore = CountDownLatch(1)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                expectedResponse = response as Response<*>
                semaphore.countDown()
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                semaphore.countDown()
                fail<String>("It should not have passed here.")
            }
        })

        semaphore.await(TIMEOUT_OF_ASYNC_TESTS_IN_MILLIS, MILLISECONDS)

        verify { mockRetrofitNetworkTracking.onSuccess(expectedResponse!!, expectedTrackingPath) }
    }

    @Test
    internal fun whenEnqueueRequestWithErrorItShouldTrackFailure() {
        val call = retrofit.create(Endpoint::class.java).get()

        var expectedThrowable: Throwable? = null
        val semaphore = CountDownLatch(1)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                semaphore.countDown()
                fail<String>("It should not have passed here.")
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {
                expectedThrowable = t
                semaphore.countDown()
            }
        })

        semaphore.await(TIMEOUT_OF_ASYNC_TESTS_IN_MILLIS, MILLISECONDS)

        verify { mockRetrofitNetworkTracking.onFailure(expectedThrowable!!, expectedTrackingPath) }
    }

    private inline fun <reified T : Any> typeOf(): Type = object : TypeToken<T>() {}.type
}