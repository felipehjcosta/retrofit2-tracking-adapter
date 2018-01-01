package com.github.felipehjcosta.retrofit2.tracking.adapter

@Retention
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class RetrofitTracking(val path: String)