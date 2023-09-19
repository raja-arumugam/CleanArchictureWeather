package com.example.cgtaskb.common

import android.widget.RemoteViews
import androidx.core.net.toUri
import com.example.cgtaskb.R

class ValidateHelperImageWidget() {

    fun widgetImage(imageUri: String, remoteViews: RemoteViews) {
        if (imageUri == "01d") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.oned)
        }

        if (imageUri == "01n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.onen)
        }

        if (imageUri == "02d") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.twod)
        }

        if (imageUri == "02n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.twon)
        }

        if (imageUri == "03d" || imageUri == "03n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.threedn)
        }

        if (imageUri == "10d") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.tend)
        }

        if (imageUri == "10n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.tenn)
        }

        if (imageUri == "04d" || imageUri == "04n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.fourdn)
        }

        if (imageUri == "09d" || imageUri == "09n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.ninedn)
        }

        if (imageUri == "11d" || imageUri == "11n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.elevend)
        }

        if (imageUri == "13d" || imageUri == "13n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.thirteend)
        }

        if (imageUri == "50d" || imageUri == "50n") {
            remoteViews.setImageViewResource(R.id.img_widget_weather, R.drawable.fiftydn)
        }

    }
}