package com.example.cgtaskb.presentation.WeatherState

import com.example.cgtaskb.data.model.CurrentWeatherResponse


data class FullWeatherReportState(
    val isLoading: Boolean = false,
    val fullWeather: CurrentWeatherResponse? = null,
    val error: String? = null
)
