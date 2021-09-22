package com.m7.forecasto.ui.forecast

import com.m7.forecasto.R
import com.m7.forecasto.base.BaseAdapter
import com.m7.forecasto.data.model.City
import com.m7.forecasto.data.model.Daily
import com.m7.forecasto.databinding.ItemForecastBinding
import com.m7.forecasto.databinding.ItemForecastCityBinding

class DailyAdapter(Dailies: List<Daily>): BaseAdapter<ItemForecastBinding, Daily>(R.layout.item_forecast, Dailies) {

    override fun bindItem(itemView: ItemForecastBinding, itemData: Daily, pos: Int) {
        itemView.apply {
            weatherTitle = itemData.weather.first().main
            weatherDesc = itemData.weather.first().description
        }
    }
}