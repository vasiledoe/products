package com.racovita.wow.utils.network

import com.google.gson.GsonBuilder
import com.racovita.wow.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {

    val retrofit: Webservice by lazy {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
        }

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_ENDPOINT)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(Webservice::class.java)
    }
}