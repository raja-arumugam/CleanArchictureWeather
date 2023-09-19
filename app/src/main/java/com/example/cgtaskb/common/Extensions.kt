package com.example.cgtaskb.common

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.cgtaskb.R
import java.text.SimpleDateFormat
import java.util.Locale

fun ImageView.loadImage(url: Int) {
    Glide.with(this)
        .load(url)
        .placeholder(context.circularProgressDrawable())
        .error(R.mipmap.ic_launcher)
        .into(this)
}

fun Context.circularProgressDrawable(): Drawable {
    return CircularProgressDrawable(this).apply {
        strokeWidth = 7f
        centerRadius = 60f
        setColorSchemeColors(
            androidx.core.content.ContextCompat.getColor(
                this@circularProgressDrawable,
                R.color.white
            )
        )
        start()
    }
}

fun getDate(dtTxt: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val date = inputFormat.parse(dtTxt)
    val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
    val dateanddayname = outputFormat.format(date!!)
    return dateanddayname
}

fun getCelsius(temp: Double): String {
    val temperatureCelsius = (temp.minus(273.15))
    val temperatureFormatted = String.format("%.2f", temperatureCelsius)
    val celsius: String = temperatureFormatted.split(".")[0]
    return celsius
}

fun showAlertDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Background Location Permission Needed")
        .setMessage("This app needs the Background Location permission, please accept to use location functionality")
        .setPositiveButton(
            "OK"
        ) { _, _ ->
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null),
                ),
            )
        }
        .create()
        .show()
}