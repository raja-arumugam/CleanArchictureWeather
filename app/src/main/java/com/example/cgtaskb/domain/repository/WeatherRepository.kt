package com.example.cgtaskb.domain.repository


import com.example.cgtaskb.data.model.CurrentWeatherResponse

interface WeatherRepository {

    suspend fun getWeather(map: HashMap<String, String>) : CurrentWeatherResponse

}