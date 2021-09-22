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
            viewBinding.tvCityName.text = city.name

            cityForecastViewModel.getForecast(city.lat, city.lng)

            cityForecastViewModel.getForecastData.observe(this) {
                handleCall(it) {
                    it.data?.let { forecast ->
                        viewBinding.apply {
                            rvDailies.layoutManager = LinearLayoutManager(this@CityForecastActivity)
                            rvDailies.adapter = DailyAdapter(forecast.daily)
                        }
                    }
                }
            }
        }
    }
}