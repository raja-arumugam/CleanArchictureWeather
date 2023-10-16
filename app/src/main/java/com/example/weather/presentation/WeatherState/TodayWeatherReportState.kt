package com.example.weather.presentation.WeatherState

import com.example.weather.data.model.WeatherList

data class TodayWeatherReportState(
    val isLoading: Boolean = false,
    val todayFullWeather: List<WeatherList?>? = null,
    val error: String? = null
)
