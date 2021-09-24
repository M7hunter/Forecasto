package com.m7.forecasto.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseViewModel
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.repo.CityRepo
import com.m7.forecasto.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val cityRepo: CityRepo) : BaseViewModel() {

    private val _searchForCityData = MutableLiveData<Resource<List<City>>>()
    val searchForCityData: LiveData<Resource<List<City>>> = _searchForCityData
    fun searchForCity(cityName: String) {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            _searchForCityData.postValue(Resource.error(msg = throwable.message.toString()))
        })) {
            if (connectivityHandler.isConnected()) {
                _searchForCityData.postValue(Resource.loading())

                cityRepo.getSuggestions(cityName).apply {
                    if (isSuccessful) {
                        val cityList = cityRepo.editResponse(body())
                        _searchForCityData.postValue(Resource.success(cityList))
                    } else {
                        _searchForCityData.postValue(Resource.error(resId = R.string.not_successful))
                    }
                }
            } else {
                _searchForCityData.postValue(Resource.error(resId = R.string.no_internet_connection))
            }
        }
    }
}