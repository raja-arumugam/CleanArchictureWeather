package com.example.cgtaskb.data.repository

import com.example.cgtaskb.data.model.CurrentWeatherResponse
import com.example.cgtaskb.data.remote.api.APIService
import com.example.cgtaskb.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: APIService,
) : WeatherRepository {

    override suspend fun getWeather(map: HashMap<String, String>): CurrentWeatherResponse {
        return apiService.getCurrentWeatherResponse(map)
    }
}