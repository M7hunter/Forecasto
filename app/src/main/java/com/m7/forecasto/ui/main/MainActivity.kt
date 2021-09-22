package com.m7.forecasto.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.model.LatLng
import com.m7.forecasto.databinding.ActivityMainBinding
import com.m7.forecasto.ui.forecast.CityForecastActivity
import com.m7.forecasto.util.Constants
import com.m7.forecasto.util.handlers.PermissionHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var permissionHandler: PermissionHandler
    private val locationPermCode = 101
    private var userCity = MutableLiveData<City?>()
    private val geocoder = Geocoder(this)

    override fun onCreation(viewBinding: ActivityMainBinding) {
        // get previously saved cities
        mainViewModel.getSavedCities()
        mainViewModel.getSavedCitiesData.observe(this) {
            handleCall(it) {
                it.data.let { cities ->
                    viewBinding.apply {
                        rvForecastCities.layoutManager = LinearLayoutManager(this@MainActivity)
                        if (!cities.isNullOrEmpty()) {
                            rvForecastCities.adapter = CityAdapter(cities) {
                                startNext(CityForecastActivity::class.java, Bundle().apply {
                                    putParcelable(Constants.selectedCity, it)
                                })
                            }
                        } else {
                            // if no cities were saved fetch the current one
                            getUserCity()
                            userCity.observe(this@MainActivity) {
                                it?.let {
                                    rvForecastCities.adapter = CityAdapter(listOf(it)) {
                                        startNext(CityForecastActivity::class.java, Bundle().apply {
                                            putParcelable(Constants.selectedCity, it)
                                        })
                                    }
                                } ?: displayError("couldn't get city, try later")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUserCity() {
        if (checkLocationPerms()) {
            userCity.postValue(getCurrentCityByLatLng())
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), locationPermCode
            )
        }
    }

    private fun checkLocationPerms() =
        permissionHandler.isGranted(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    private fun getCurrentCityByLatLng() =
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
                LatLng(it.latitude, it.longitude)
                geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    .firstNotNullOf {
                        City(1, it.adminArea, it.latitude, it.longitude)
                    }
            }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermCode)
            userCity.postValue(
                if (checkLocationPerms()) {
                    getCurrentCityByLatLng()
                } else {
                    getDefaultCityByName()
                }
            )
    }

    private fun getDefaultCityByName() =
        geocoder.getFromLocationName("London, UK", 1)
            .firstNotNullOf {
                City(1, "London, UK", it.latitude, it.longitude)
            }

}