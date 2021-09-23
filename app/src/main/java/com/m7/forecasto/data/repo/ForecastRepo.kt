package com.m7.forecasto.data.repo

import com.m7.forecasto.data.api.APIsImpl
import com.m7.forecasto.data.db.WeatherDao
import com.m7.forecasto.data.model.Forecast
import com.m7.forecasto.data.model.Weather
import javax.inject.Inject

class ForecastRepo @Inject constructor(
    private val apIsImpl: APIsImpl,
    private val weatherDao: WeatherDao
) {

    suspend fun getForecastByLatLng(lat: Double, lng: Double) =
        apIsImpl.getForecastByLatLng(lat, lng)

    // pass all required data to weather object to save it in the db
    fun editResponse(forecast: Forecast?, cityId: Int): List<Weather> {
        val weatherList = ArrayList<Weather>()
        forecast?.daily?.forEach {
            it.weather.firstOrNull()?.let { weather ->
                weather.cityId = cityId
                weather.lat = forecast.lat
                weather.lon = forecast.lon

                weatherList.add(weather)
            }
        }

        return weatherList
    }

    suspend fun saveWeatherList(weatherList: List<Weather>) =
        weatherDao.saveWeatherList(weatherList)

    suspend fun getWeatherListByCityId(cityId: Int) =
        weatherDao.getWeatherListByCityId(cityId)

}