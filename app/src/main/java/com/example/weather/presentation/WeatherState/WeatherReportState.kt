package com.example.weather.presentation.WeatherState

import com.example.weather.data.model.WeatherList


data class WeatherReportState(
    val isLoading: Boolean = false,
//    val weather: CurrentWeatherResponse? = null,
    val weather:  List<WeatherList?>? = null,
    val error: String? = null
)
