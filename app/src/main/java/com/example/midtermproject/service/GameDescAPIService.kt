package com.example.midtermproject.service

import com.example.midtermproject.model.GameClass
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import io.reactivex.rxjava3.core.Single

import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GameDescAPIService {


    private val BASE_URL  = "https://api.rawg.io/api/"

    private val apiDet = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build().create(GameDescAPI:: class.java)


    fun getDetData(query: String): Single<GameClass> {
        return apiDet.getGame(query)

    }

}