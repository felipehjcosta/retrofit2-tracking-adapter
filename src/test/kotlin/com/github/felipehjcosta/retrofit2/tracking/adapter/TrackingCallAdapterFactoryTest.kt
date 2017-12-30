package com.github.felipehjcosta.retrofit2.tracking.adapter

import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit


class TrackingCallAdapterFactoryTest {

    companion object {
        private val NO_ANNOTATIONS = emptyArray<Annotation>()
    }

    private val server = MockWebServer()

    private val factory = TrackingCallAdapterFactory()

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
    internal fun ensureAdapterCreationWhenCallGet() {
        val adapter = factory.get(String::class.java, NO_ANNOTATIONS, retrofit)
        assertNotNull(adapter)
    }
}