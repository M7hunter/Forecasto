package com.m7.forecasto.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.data.model.LatLng
import com.m7.forecasto.databinding.ActivityMainBinding
import com.m7.forecasto.util.handlers.PermissionHandler
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var permissionHandler: PermissionHandler

    private val locationPermCode = 101

    private var userLatLng = MutableLiveData<LatLng?>()

    override fun onCreation(viewBinding: ActivityMainBinding) {
        getLocation()

        userLatLng.observe(this) {
            it?.let {
                mainViewModel.getForecast(it.lat, it.lng)
            }
        }

    }

    private fun getLocation() {
        if (checkLocationPerms()) {
            userLatLng.postValue(getCurrentLocationLatLng())
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
    private fun getCurrentLocationLatLng() =
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let {
                LatLng(it.latitude, it.longitude)
            }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermCode)
            userLatLng.postValue(
                if (checkLocationPerms()) {
                    getCurrentLocationLatLng()
                } else {
                    getDefaultLocationLatLng()
                }
            )
    }

    private fun getDefaultLocationLatLng() =
        Geocoder(this).getFromLocationName("London, UK", 1)
            .firstNotNullOf {
                LatLng(it.latitude, it.longitude)
            }

}