package com.m7.forecasto.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.data.model.City
import com.m7.forecasto.databinding.ActivityMainBinding
import com.m7.forecasto.ui.forecast.CityForecastActivity
import com.m7.forecasto.util.Constants
import com.m7.forecasto.util.Logger
import com.m7.forecasto.util.handlers.PermissionHandler
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var permissionHandler: PermissionHandler
    private val locationPermCode = 101
    private var userCity = MutableLiveData<City?>()
    private val geocoder = Geocoder(this, Locale.getDefault())

    override fun onCreation(viewBinding: ActivityMainBinding) {
        // get previously saved cities
        mainViewModel.getSavedCities()
        mainViewModel.getSavedCitiesData?.observe(this) {
            it.let { cities ->
                viewBinding.apply {
                    rvCities.layoutManager = LinearLayoutManager(this@MainActivity)
                    if (!cities.isNullOrEmpty()) {
                        rvCities.adapter =
                            CityAdapter(
                                cities.takeIf { it.size >= 5 }?.subList(0, 5) ?: cities
                            ) {
                                startNext(CityForecastActivity::class.java, Bundle().apply {
                                    putParcelable(Constants.selectedCity, it)
                                })
                            }
                    } else {
                        // if no cities were saved get the current one
                        getUserCity()
                        userCity.observe(this@MainActivity) {
                            it?.let {
                                rvCities.adapter = CityAdapter(listOf(it)) {
                                    startNext(CityForecastActivity::class.java, Bundle().apply {
                                        putParcelable(Constants.selectedCity, it)
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUserCity() {
        if (checkLocationPerms()) {
            getCurrentCityByLatLng()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermCode)
            if (checkLocationPerms()) {
                getCurrentCityByLatLng()
            } else {
                getDefaultCityByName()
            }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentCityByLatLng() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnSuccessListener {
                Logger.d("location", "success")

                // get address by lat & lng
                geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    .firstOrNull()?.let {
                        userCity.postValue(City(1, it.latitude, it.longitude, it.adminArea))
                    }
                // some cases doesn't represent an address
                    ?: userCity.postValue(
                        City(
                            1,
                            it.latitude,
                            it.longitude,
                            getString(R.string.current_city)
                        )
                    )
            }
            .addOnFailureListener {
                displayError(getString(R.string.couldnt_get_city))
            }
    }

    private fun getDefaultCityByName() =
        try {
            geocoder.getFromLocationName(getString(R.string.default_city_name), 1)
                .firstNotNullOf {
                    userCity.postValue(
                        City(1, it.latitude, it.longitude, getString(R.string.default_city_name))
                    )
                }
        } catch (e: IOException) {
            displayError(getString(R.string.no_internet_connection))
        }
}

