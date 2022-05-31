package com.cpsai.zohotask.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpBuilder = OkHttpClient.Builder()
            .connectTimeout(90,TimeUnit.SECONDS)
            .writeTimeout(90,TimeUnit.SECONDS)
            .readTimeout(90,TimeUnit.SECONDS)
        httpBuilder.addInterceptor(logging)
        httpBuilder.addInterceptor{chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("charset","UTF-8")
                .build()
            chain.proceed(request)
        }
        val client = httpBuilder.build()
        return Retrofit.Builder()
            .baseUrl("http://demo2143341.mockable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiServie: ApiService = getRetrofit().create(ApiService::class.java)
}