package com.example.weather.presentation.activity

import android.Manifest
import android.R.attr.text
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.common.ValidateHelperImage
import com.example.weather.common.getCelsius
import com.example.weather.common.getDate
import com.example.weather.data.model.LocationEvent
import com.example.weather.data.model.WeatherList
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.di.component.app.Injectable
import com.example.weather.di.component.viewmodel.injectViewModel
import com.example.weather.presentation.LocationService
import com.example.weather.presentation.adapter.ForeCastAdapter
import com.example.weather.presentation.viewmodel.MainActivityViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityMainBinding
    private var service: Intent? = null
    private lateinit var mViewModel: MainActivityViewModel
    private lateinit var mAdapter: ForeCastAdapter

    private val currentDateTime = LocalDateTime.now()
    private val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        AndroidInjection.inject(this)

        mViewModel = injectViewModel(viewModelFactory)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        service = Intent(this, LocationService::class.java)
        checkPermissions()
    }

    private fun checkPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    ) && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            checkBackgroundLocation()
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED
                    ) && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkBackgroundLocation()
                    } else {
                        checkLocationPermission()
                    }

                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.packageName, null),
                        ),
                    )

                } else {
                    requestLocationPermission()
                }
                return
            }

            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startService(service)
                        binding.ProgressBar.visibility = View.VISIBLE
                    }
                } else {

                    AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.background_location_need))
                        .setMessage(resources.getString(R.string.background_location_need_desc))
                        .setPositiveButton(
                            resources.getString(R.string.general_ok)
                        ) { _, _ ->
                            checkBackgroundLocation()
                        }
                        .create()
                        .show()
                }
                return
            }
        }
    }

    private fun getClosestWeatherResponse(weatherResponse: List<WeatherList?>) {

        val prefs: SharedPreferences.Editor =
            getSharedPreferences(resources.getString(R.string.weatherData), MODE_PRIVATE).edit()

        with(binding) {
            val currentDate = currentDateO

            // show Closest Weather Data
            for (i in weatherResponse) {
                if (i?.dt_txt?.split("\\s".toRegex())?.contains(currentDate) == true) {
                    tvDate.text = getDate(i.dt_txt)

                    prefs.putString(resources.getString(R.string.date), getDate(i.dt_txt))
                    prefs.commit()
                }

                // Show Temperature
                tvWeatherTemp.text = i?.main?.temp?.let { getCelsius(it) } + "°C"

                prefs.putString(
                    resources.getString(R.string.temperature),
                    i?.main?.temp?.let { getCelsius(it) } + "°C")
                prefs.commit()

                tvHumidity.text = "${i?.main?.humidity}%"
                tvWindSpeed.text = "${i?.wind?.speed}%"
                tvChanceRain.text = "${i?.pop}%"

                // Show Current weather state & weather image
                for (j in i?.weather!!) {

                    binding.tvWeatherState.text = j.description

                    ValidateHelperImage().image(j.icon, binding)

                    prefs.putString(resources.getString(R.string.temperature_desc), j.description)
                    prefs.commit()

                    prefs.putString(resources.getString(R.string.temperature_pic), j.icon)
                    prefs.commit()
                }
            }
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        } else {
            startService(service)
            binding.ProgressBar.visibility = View.VISIBLE
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent) {

        mViewModel.getWeather(locationEvent.latitude, locationEvent.longitude)

        lifecycleScope.launch {
            mViewModel.closestWeatherState.collectLatest { uiState ->

                uiState.weather?.let {
                    binding.ProgressBar.visibility = View.GONE
                    binding.llMain.visibility = View.VISIBLE
                    binding.llRv.visibility = View.VISIBLE
                    getClosestWeatherResponse(it)
                }

                if (uiState.error != null) {
                    binding.ProgressBar.visibility = View.GONE
                    binding.llMain.visibility = View.GONE
                    binding.llRv.visibility = View.GONE
                    Toast.makeText(this@MainActivity, uiState.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                if (uiState.isLoading) {
                    binding.ProgressBar.visibility = View.VISIBLE
                    binding.llMain.visibility = View.GONE
                    binding.llRv.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.todayWeatherState.collectLatest { todayWeatherUIState ->

                todayWeatherUIState.todayFullWeather?.let {
                    binding.llMain.visibility = View.VISIBLE
                    binding.llRv.visibility = View.VISIBLE
                    mAdapter = ForeCastAdapter(it)
                    binding.rvForecast.adapter = mAdapter
                }

                if (todayWeatherUIState.error != null) {
                    Toast.makeText(
                        this@MainActivity,
                        todayWeatherUIState.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.fullWeatherState.collectLatest { uiState ->

                uiState.fullWeather?.let {
                    binding.llMain.visibility = View.VISIBLE
                    binding.llRv.visibility = View.VISIBLE

                    binding.cityName.text = it.city.name

                    val prefs: SharedPreferences.Editor =
                        getSharedPreferences(
                            resources.getString(R.string.weatherData),
                            MODE_PRIVATE
                        ).edit()
                    prefs.putString(resources.getString(R.string.city), it.city.name)
                    prefs.commit()
                }

                if (uiState.error != null) {
                    Toast.makeText(this@MainActivity, uiState.error.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}