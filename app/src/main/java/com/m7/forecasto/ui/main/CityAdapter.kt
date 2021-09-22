package com.m7.forecasto.ui.main

import com.m7.forecasto.R
import com.m7.forecasto.base.BaseAdapter
import com.m7.forecasto.data.model.City
import com.m7.forecasto.databinding.ItemForecastCityBinding

class CityAdapter(cities: List<City>, private val onItemClick: (city: City) -> Unit) :
    BaseAdapter<ItemForecastCityBinding, City>(R.layout.item_forecast_city, cities) {

    override fun bindItem(itemView: ItemForecastCityBinding, itemData: City, pos: Int) {
        itemView.apply {
            cityName = itemData.name

            root.setOnClickListener {
                onItemClick.invoke(itemData)
            }
        }
    }
}