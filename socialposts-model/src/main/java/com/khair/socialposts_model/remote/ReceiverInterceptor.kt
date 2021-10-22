package com.khair.socialposts_model.remote

import okhttp3.Interceptor
import okhttp3.Response

class ReceiverInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}