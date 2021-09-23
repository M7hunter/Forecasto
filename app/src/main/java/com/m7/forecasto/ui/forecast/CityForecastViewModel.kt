package com.m7.forecasto.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseViewModel
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.model.Weather
import com.m7.forecasto.data.repo.CityRepo
import com.m7.forecasto.data.repo.ForecastRepo
import com.m7.forecasto.util.Resource
import com.m7.forecasto.util.handlers.ConnectivityHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityForecastViewModel @Inject constructor(
    private val forecastRepo: ForecastRepo,
    private val cityRepo: CityRepo
) : BaseViewModel() {

    @Inject
    lateinit var connectivityHandler: ConnectivityHandler

    private val _getForecastData = MutableLiveData<Resource<List<Weather>>>()
    val getForecastData: LiveData<Resource<List<Weather>>> = _getForecastData
    fun getForecast(lat: Double, lng: Double, cityId: Int) {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            _getForecastData.postValue(Resource.error(msg = throwable.message.toString()))
        })) {
            if (connectivityHandler.isConnected()) {
                _getForecastData.postValue(Resource.loading())

                forecastRepo.getForecastByLatLng(lat, lng).apply {
                    if (isSuccessful) {
                        val weatherList = forecastRepo.editResponse(body(), cityId)
                        // save updated list for offline usage
                        forecastRepo.saveWeatherList(weatherList)
                        // update ui with latest data
                        _getForecastData.postValue(Resource.success(weatherList))
                    } else {
                        _getForecastData.postValue(Resource.error(resId = R.string.not_successful))
                    }
                }
            } else {
                _getForecastData.postValue(Resource.error(resId = R.string.no_internet_connection))
                // update ui with saved data
                _getForecastData.postValue(
                    Resource.success(forecastRepo.getWeatherListByCityId(cityId))
                )
            }
        }
    }

    private val _saveCityData = MutableLiveData<Resource<Unit>>()
    val saveCityData: LiveData<Resource<Unit>> = _saveCityData
    fun saveCity(city: City) {
        dbCall(_saveCityData) { cityRepo.saveCity(city) }
    }

    private val _removeCityData = MutableLiveData<Resource<Unit>>()
    val removeCityData: LiveData<Resource<Unit>> = _removeCityData
    fun removeCity(city: City) {
        dbCall(_removeCityData) { cityRepo.removeCity(city) }
    }
}