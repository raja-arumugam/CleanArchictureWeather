package com.example.weather.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.example.weather.R
import com.example.weather.common.ValidateHelperImageWidget
import com.example.weather.presentation.activity.MainActivity

class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds.forEach { appWidgetId ->
            updateWeatherAppWidget(context, appWidgetId, appWidgetManager)
        }
    }

    companion object {

        private val TAG = WeatherWidget::class.java.simpleName

        private fun getWidgetPendingIntent(context: Context): PendingIntent {
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            // Create an Intent to launch MainActivity
            return PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                flags
            )
        }

        fun updateWeatherAppWidget(
            context: Context,
            appWidgetId: Int,
            appWidgetManager: AppWidgetManager,
        ) {
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_weather
            ).apply {
                setOnClickPendingIntent(R.id.ll_weather, getWidgetPendingIntent(context))
            }

            val prefs = context.getSharedPreferences(context.resources.getString(R.string.weatherData), MODE_PRIVATE)

            remoteViews.setTextViewText(
                R.id.tv_weather_widget_temp,
                prefs.getString(context.resources.getString(R.string.temperature), "")
            )

            remoteViews.setTextViewText(
                R.id.tv_weather_widget_state,
                prefs.getString(context.resources.getString(R.string.temperature_desc), "")
            )

            remoteViews.setTextViewText(
                R.id.tv_weather_widget_date,
                prefs.getString(context.resources.getString(R.string.date), "")
            )

            remoteViews.setTextViewText(
                R.id.tv_widget_city,
                prefs.getString(context.resources.getString(R.string.city), "")
            )

            val prefers = prefs.getString(context.resources.getString(R.string.temperature_pic), "")
            prefers?.let {
                ValidateHelperImageWidget().widgetImage(
                    it, remoteViews
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

}