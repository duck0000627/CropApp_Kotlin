package com.example.cropapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 伺服器基礎網址 (Base URL)
    private const val BASE_URL = "https://1v3cl5x5-7091.jpe1.devtunnels.ms/"

    // 建立 Retrofit 實體，並指定把 JSON 轉換成 Gson
    val apiService: CropApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CropApiService::class.java)
    }
}