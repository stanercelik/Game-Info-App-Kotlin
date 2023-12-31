package com.example.midtermproject.service

import com.example.midtermproject.model.GameClass
import com.squareup.moshi.Json

class Responses (
    @Json(name = "count") val count: Int?,

    @Json(name = "results") var results: List<GameClass>?,

    )