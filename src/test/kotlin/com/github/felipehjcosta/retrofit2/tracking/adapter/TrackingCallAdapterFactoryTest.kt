package com.github.felipehjcosta.retrofit2.tracking.adapter

import com.google.common.reflect.TypeToken
import io.mockk.mockk
import io.mockk.verify
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit
import java.lang.reflect.Type


class TrackingCallAdapterFactoryTest {

    companion object {
        private val NO_ANNOTATIONS = emptyArray<Annotation>()
    }

    private val server = MockWebServer()

    private val mockRetrofitNetworkTracking = mockk<RetrofitNetworkTracking>(relaxed = true)

    private val factory = TrackingCallAdapterFactory { mockRetrofitNetworkTracking }

    private lateinit var retrofit: Retrofit

    @BeforeEach
    internal fun setUp() {
        server.start()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
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
    internal fun whenMakeARequestWithSuccessItShouldTrackSuccess() {
        server.enqueue(MockResponse().setBody(""))

        val call = retrofit.create(Endpoint::class.java).get()

        call.execute()

        verify { mockRetrofitNetworkTracking.onSuccess(any()) }
    }

    @Test
    internal fun whenMakeARequestWithErrorItShouldTrackFailure() {
        val call = retrofit.create(Endpoint::class.java).get()

        assertThrows(Exception::class.java) { call.execute() }

        verify { mockRetrofitNetworkTracking.onFailure(any()) }
    }

    private inline fun <reified T : Any> typeOf(): Type = object : TypeToken<T>() {}.type
}