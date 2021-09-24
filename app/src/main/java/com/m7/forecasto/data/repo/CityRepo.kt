package com.m7.forecasto.data.repo

import com.m7.forecasto.data.api.SuggestionAPIsImpl
import com.m7.forecasto.data.db.CityDao
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.model.SuggestionsRes
import javax.inject.Inject

class CityRepo @Inject constructor(
    private val suggestionAPIsImpl: SuggestionAPIsImpl,
    private val cityDao: CityDao
) {

    suspend fun getSuggestions(text: String) =
        suggestionAPIsImpl.getSuggestions(text)

    suspend fun editResponse(body: SuggestionsRes?): List<City> {
        val cityList = ArrayList<City>()
        body?.let {
            it.results?.forEach { location ->
                location.geometry?.location?.let {
                    cityList.add(City(getNewId(), it.lat.toDouble(), it.lng.toDouble(), location.name))
                }
            }
        }
        return cityList
    }

    // a workaround to have a unique id for the new saved data as i don't own the api data
    private suspend fun getNewId() = (cityDao.getIds().maxOrNull() ?: 1) + 1

    suspend fun saveCity(city: City) = cityDao.saveCity(city)

    suspend fun removeCity(city: City) = cityDao.removeCity(city)

    fun getSavedCities() = cityDao.getSavedCities()
}