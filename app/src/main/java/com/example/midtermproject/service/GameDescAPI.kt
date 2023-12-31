package com.example.midtermproject.service

import com.example.midtermproject.model.GameClass
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GameDescAPI {
    @GET("games/{query}?key=3be8af6ebf124ffe81d90f514e59856c")
    fun getGame(@Path("query") query:  String?): Single<GameClass>

}