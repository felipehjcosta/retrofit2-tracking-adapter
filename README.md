Retrofit 2 Tracking Library
==================================

[![Build Status](https://travis-ci.org/felipehjcosta/retrofit2-tracking-adapter.svg?branch=master)](https://travis-ci.org/felipehjcosta/retrofit2-tracking-adapter) [![codecov](https://codecov.io/gh/felipehjcosta/retrofit2-tracking-adapter/branch/master/graph/badge.svg)](https://codecov.io/gh/felipehjcosta/retrofit2-tracking-adapter) [![codebeat badge](https://codebeat.co/badges/fe01dfd5-be8b-4326-b976-09933221c39e)](https://codebeat.co/projects/github-com-felipehjcosta-retrofit2-tracking-adapter-master)

A Retrofit 2 tracking requests library.

Usage
-----

Annotate the interface with the `Tracking` annotation:
```kotlin
interface Endpoint {
    @GET("/test/1")
    @Tracking("/test/{id}")
    fun get(): Call<String>
}
```

Create an instance of `TrackingCallAdapterFactory` and add it to Retrofit Builder.
```kotlin
val factory = TrackingCallAdapterFactory {
            object : RetrofitNetworkTracking {
                override fun onSuccess(response: Response<*>, trackingPath: String?) {
                    // track success response
                }

                override fun onFailure(throwable: Throwable, trackingPath: String?) {
                    // track failure response
                }
            }
        }

val okHttpClient = OkHttpClient.Builder().build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .client(okHttpClient)
    .addCallAdapterFactory(factory)
    .build()
```

You can use the interceptor instead of `TrackingCallAdapterFactory` if you prefer:
```kotlin
val interceptor = TrackingRequestInterceptor {
            object : NetworkRequestTracking {
                override fun onRequest(request: Request, response: Response, trackingPath: String?) {
                    // track request and response with path response
                }
            }
        }

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .client(okHttpClient)
    .build()
```

Download
--------

Gradle:
```groovy
compile 'com.github.felipehjcosta:retrofit2-tracking-adapter:1.0.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.github.felipehjcosta</groupId>
  <artifactId>retrofit2-tracking-adapter</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

License
-------

MIT License

Copyright (c) 2017 Felipe Costa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
