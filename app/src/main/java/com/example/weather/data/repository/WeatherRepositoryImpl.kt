package com.example.weather.data.repository

import com.example.weather.data.model.CurrentWeatherResponse
import com.example.weather.data.remote.api.APIService
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: APIService,
) : WeatherRepository {

    override suspend fun getWeather(map: HashMap<String, String>): CurrentWeatherResponse {
        return apiService.getCurrentWeatherResponse(map)
    }
}