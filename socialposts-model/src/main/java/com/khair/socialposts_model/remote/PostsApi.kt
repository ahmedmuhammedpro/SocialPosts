package com.khair.socialposts_model.remote

import com.khair.socialposts_model.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PostsApi {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun getPostsApi(): PostsApiRest {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttp())
            .build()

        return retrofit.create(PostsApiRest::class.java)
    }

    private fun createOkHttp(): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(ReceiverInterceptor())

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okBuilder.addInterceptor(loggingInterceptor)
        }

        return okBuilder.build()
    }
}