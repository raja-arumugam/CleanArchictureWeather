package com.example.weather.presentation.WeatherState

import com.example.weather.data.model.CurrentWeatherResponse


data class FullWeatherReportState(
    val isLoading: Boolean = false,
    val fullWeather: CurrentWeatherResponse? = null,
    val error: String? = null
)
