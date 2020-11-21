package com.example.submission2.util.api

import com.example.submission2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val token = BuildConfig.token
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "token $token")
                .build()
        )
    }
}