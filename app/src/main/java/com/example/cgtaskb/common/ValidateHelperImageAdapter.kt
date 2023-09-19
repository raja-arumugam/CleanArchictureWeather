package com.example.cgtaskb.common

import com.example.cgtaskb.R
import com.example.cgtaskb.databinding.LayoutForecastBinding

class ValidateHelperImageAdapter() {

    fun image (image: String, binding: LayoutForecastBinding) {
        if (image == "01d") {
            binding.imgWeather.loadImage(R.drawable.oned)
        }

        if (image == "01n") {
            binding.imgWeather.loadImage(R.drawable.onen)
        }

        if (image == "02d") {
            binding.imgWeather.loadImage(R.drawable.twod)
        }

        if (image == "02n") {
            binding.imgWeather.loadImage(R.drawable.twon)
        }

        if (image == "03d" || image == "03n") {
            binding.imgWeather.loadImage(R.drawable.threedn)
        }

        if (image == "10d") {
            binding.imgWeather.loadImage(R.drawable.tend)
        }

        if (image == "10n") {
            binding.imgWeather.loadImage(R.drawable.tenn)
        }

        if (image == "04d" || image == "04n") {
            binding.imgWeather.loadImage(R.drawable.fourdn)
        }

        if (image == "09d" || image == "09n") {
            binding.imgWeather.loadImage(R.drawable.ninedn)
        }

        if (image == "11d" || image == "11n") {
            binding.imgWeather.loadImage(R.drawable.elevend)
        }

        if (image == "13d" || image == "13n") {
            binding.imgWeather.loadImage(R.drawable.thirteend)
        }

        if (image == "50d" || image == "50n") {
            binding.imgWeather.loadImage(R.drawable.fiftydn)
        }

    }
}