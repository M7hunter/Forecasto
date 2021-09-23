package com.m7.forecasto.ui.forecast

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.data.model.City
import com.m7.forecasto.databinding.ActivityCityForecastBinding
import com.m7.forecasto.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityForecastActivity :
    BaseActivity<ActivityCityForecastBinding>(R.layout.activity_city_forecast) {

    private val cityForecastViewModel by viewModels<CityForecastViewModel>()

    override fun onCreation(viewBinding: ActivityCityForecastBinding) {
        intent.extras?.getParcelable<City>(Constants.selectedCity)?.let { city ->
            viewBinding.apply {
                cityName = city.name
                isFav = city.isFavorite

                ivFav.setOnClickListener {
                    ivFav.isEnabled = false
                    if (city.isFavorite) {
                        cityForecastViewModel.removeCity(city)
                    } else {
                        cityForecastViewModel.saveCity(city)
                    }
                }

                cityForecastViewModel.removeCityData.observe(this@CityForecastActivity) {
                    isFav = false
                    ivFav.isEnabled = true
                    city.isFavorite = false
                }

                cityForecastViewModel.saveCityData.observe(this@CityForecastActivity) {
                    isFav = true
                    ivFav.isEnabled = true
                    city.isFavorite = true
                }
            }

            cityForecastViewModel.getForecast(city.lat, city.lng, city.id)
            cityForecastViewModel.getForecastData.observe(this) {
                handleCall(it) {
                    it.data?.let { weatherList ->
                        viewBinding.rvDailies.apply {
                            layoutManager = LinearLayoutManager(this@CityForecastActivity)
                            adapter = DailyAdapter(
                                weatherList.takeIf { it.size >= 5 }?.subList(0, 5)
                                    ?: weatherList
                            )
                        }
                    }
                }
            }
        }
    }
}