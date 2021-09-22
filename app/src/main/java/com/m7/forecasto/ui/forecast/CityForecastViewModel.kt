package com.m7.forecasto.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.m7.forecasto.base.BaseViewModel
import com.m7.forecasto.data.model.Forecast
import com.m7.forecasto.data.repo.ForecastRepo
import com.m7.forecasto.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityForecastViewModel @Inject constructor(
    private val repo: ForecastRepo
) : BaseViewModel() {

    private val _getForecastData = MutableLiveData<Resource<Forecast>>()
    val getForecastData: LiveData<Resource<Forecast>> = _getForecastData
    fun getForecast(lat: Double, lng: Double) {
        call(_getForecastData) { repo.getForecastByLatLng(lat, lng) }
    }
}