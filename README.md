Retrofit 2 Tracking Adapter
==================================

[![Build Status](https://travis-ci.org/felipehjcosta/retrofit2-tracking-adapter.svg?branch=master)](https://travis-ci.org/felipehjcosta/retrofit2-tracking-adapter) [![codecov](https://codecov.io/gh/felipehjcosta/retrofit2-tracking-adapter/branch/master/graph/badge.svg)](https://codecov.io/gh/felipehjcosta/retrofit2-tracking-adapter) [![codebeat badge](https://codebeat.co/badges/fe01dfd5-be8b-4326-b976-09933221c39e)](https://codebeat.co/projects/github-com-felipehjcosta-retrofit2-tracking-adapter-master)

A Retrofit 2 adapter for tracking responses.

Usage
-----

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

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .addCallAdapterFactory(factory)
    .build()
```

Download
--------

Gradle:
```groovy
compile 'com.github.felipehjcosta:retrofit2-tracking-adapter:0.9.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.github.felipehjcosta</groupId>
  <artifactId>retrofit2-tracking-adapter</artifactId>
  <version>0.9.0</version>
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
