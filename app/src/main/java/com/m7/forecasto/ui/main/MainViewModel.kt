package com.m7.forecasto.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.base.BaseViewModel
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.repo.CityRepo
import com.m7.forecasto.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CityRepo
) : BaseViewModel() {

    private val _getSavedCitiesData = MutableLiveData<Resource<List<City>>>()
    val getSavedCitiesData: LiveData<Resource<List<City>>> = _getSavedCitiesData
    fun getSavedCities() {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            _getSavedCitiesData.postValue(Resource.error(msg = throwable.message.toString()))
        })) {
            _getSavedCitiesData.postValue(Resource.loading())
            _getSavedCitiesData.postValue(Resource.success(repo.getSavedCities()))
        }
    }
}