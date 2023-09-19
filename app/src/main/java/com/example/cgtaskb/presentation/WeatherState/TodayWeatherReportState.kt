package com.example.cgtaskb.presentation.WeatherState

import com.example.cgtaskb.data.model.WeatherList

data class TodayWeatherReportState(
    val isLoading: Boolean = false,
    val todayFullWeather: List<WeatherList?>? = null,
    val error: String? = null
)
