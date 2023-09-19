package com.example.cgtaskb.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cgtaskb.BuildConfig
import com.example.cgtaskb.common.DataState
import com.example.cgtaskb.data.model.WeatherList
import com.example.cgtaskb.domain.usecase.GetWeatherUseCase
import com.example.cgtaskb.presentation.WeatherState.FullWeatherReportState
import com.example.cgtaskb.presentation.WeatherState.TodayWeatherReportState
import com.example.cgtaskb.presentation.WeatherState.WeatherReportState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
class MainActivityViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _closestWeatherState = MutableStateFlow(WeatherReportState())
    val closestWeatherState: StateFlow<WeatherReportState> = _closestWeatherState.asStateFlow()

    private val _todayWeatherState = MutableStateFlow(TodayWeatherReportState())
    val todayWeatherState: StateFlow<TodayWeatherReportState> = _todayWeatherState.asStateFlow()

    private val _fullWeatherState = MutableStateFlow(FullWeatherReportState())
    val fullWeatherState: StateFlow<FullWeatherReportState> = _fullWeatherState.asStateFlow()

    val currentDateTime = LocalDateTime.now()
    val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    private var job: Job? = null

    fun getWeather(latitude: Double?, longitude: Double?) {
        job?.cancel()
        val currentDate = currentDateO
        val closestWeatherList = mutableListOf<WeatherList>()
        val todayWeatherList = mutableListOf<WeatherList>()

        val map: HashMap<String, String> = HashMap()
        map["lat"] = latitude.toString()
        map["lon"] = longitude.toString()
        map["appid"] = BuildConfig.API_KEY

        job = getWeatherUseCase(map).onEach { result ->
            when (result) {
                is DataState.Success -> {

                    // Closest current weather for a Day.
                    _closestWeatherState.update { it.copy(isLoading = false) }
                    _closestWeatherState.update { it.copy(error = null) }
                    result.data?.list?.forEach { todayWeather ->
                        if (todayWeather.dt_txt.split("\\s".toRegex()).contains(currentDate)) {
                            closestWeatherList.add(todayWeather)
                        }

                        val closestWeather = findClosestWeather(closestWeatherList)
                        _closestWeatherState.update {
                            it.copy(weather = listOf(closestWeather))
                        }
                    }

                    // Today Weather for a Day.
                    _todayWeatherState.update { it.copy(isLoading = false) }
                    _todayWeatherState.update { it.copy(error = null) }
                    result.data?.list?.forEach { todayWeather ->
                        if (todayWeather.dt_txt.split("\\s".toRegex()).contains(currentDate)) {
                            todayWeatherList.add(todayWeather)
                        }

                        _todayWeatherState.update {
                            it.copy(todayFullWeather = todayWeatherList)
                        }
                    }

                    _fullWeatherState.update { it.copy(isLoading = false) }
                    _fullWeatherState.update { it.copy(error = null) }
                    result.data.let {
                        _fullWeatherState.update { it.copy(fullWeather = result.data) }
                    }

                }

                is DataState.Error -> {
                    _closestWeatherState.update { it.copy(isLoading = false) }
                    _closestWeatherState.update { it.copy(error = result.message) }

                    _todayWeatherState.update { it.copy(isLoading = false) }
                    _todayWeatherState.update { it.copy(error = result.message) }

                    _fullWeatherState.update { it.copy(isLoading = false) }
                    _fullWeatherState.update { it.copy(error = result.message) }
                }

                is DataState.Loading -> {
                    _closestWeatherState.update { it.copy(isLoading = true) }
                    _todayWeatherState.update { it.copy(isLoading = true) }
                    _fullWeatherState.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun findClosestWeather(weatherList: List<WeatherList>): WeatherList? {
        val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather: WeatherList? = null
        var minTimeDifference = Int.MAX_VALUE

        for (weather in weatherList) {
            val weatherTime = weather.dt_txt.substring(11, 16)
            val timeDifference = abs(timeToMinutes(weatherTime) - timeToMinutes(systemTime))

            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
                closestWeather = weather
            }
        }

        return closestWeather
    }

    private fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }

}