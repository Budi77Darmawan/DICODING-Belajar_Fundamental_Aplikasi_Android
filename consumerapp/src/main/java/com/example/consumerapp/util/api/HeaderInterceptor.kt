package com.example.consumerapp.util.api

import com.example.consumerapp.BuildConfig
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