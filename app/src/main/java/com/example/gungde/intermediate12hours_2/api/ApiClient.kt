package com.reon.app.reon.api

import android.content.Context
import com.example.gungde.intermediate12hours_2.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by gungdeaditya on 11/9/17.
 */
class ApiClient {
    fun getClient(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}