package com.m7.forecasto.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.base.BaseViewModel
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.repo.CityRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CityRepo
) : BaseViewModel() {

    var getSavedCitiesData: LiveData<List<City>>? = null
    fun getSavedCities() {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        })) {
            getSavedCitiesData = repo.getSavedCities()
        }
    }
}