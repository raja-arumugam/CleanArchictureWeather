package com.example.weather.data.remote.api

import com.example.weather.data.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIService {

    @GET("forecast?")
    suspend fun getCurrentWeatherResponse(
        @QueryMap map: HashMap<String, String>
    ): CurrentWeatherResponse


}