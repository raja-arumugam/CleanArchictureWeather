package com.example.cgtaskb.presentation.WeatherState

import com.example.cgtaskb.data.model.WeatherList


data class WeatherReportState(
    val isLoading: Boolean = false,
//    val weather: CurrentWeatherResponse? = null,
    val weather:  List<WeatherList?>? = null,
    val error: String? = null
)
