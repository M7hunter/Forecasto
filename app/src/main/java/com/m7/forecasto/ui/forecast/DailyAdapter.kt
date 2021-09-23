package com.m7.forecasto.ui.forecast

import com.m7.forecasto.R
import com.m7.forecasto.base.BaseAdapter
import com.m7.forecasto.data.model.Weather
import com.m7.forecasto.databinding.ItemForecastBinding

class DailyAdapter(Dailies: List<Weather>): BaseAdapter<ItemForecastBinding, Weather>(R.layout.item_forecast, Dailies) {

    override fun bindItem(itemView: ItemForecastBinding, itemData: Weather, pos: Int) {
        itemView.apply {
            weatherTitle = itemData.main
            weatherDesc = itemData.description
        }
    }
}