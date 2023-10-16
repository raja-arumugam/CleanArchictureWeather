package com.example.weather.domain.repository


import com.example.weather.data.model.CurrentWeatherResponse

interface WeatherRepository {

    suspend fun getWeather(map: HashMap<String, String>) : CurrentWeatherResponse

}