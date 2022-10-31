package com.example.listnhac.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val api:SongApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://musicapi123.000webhostapp.com/Server/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongApi::class.java)
    }
}

