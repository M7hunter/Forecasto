package com.m7.forecasto.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.databinding.ActivitySearchBinding
import com.m7.forecasto.ui.forecast.CityForecastActivity
import com.m7.forecasto.ui.main.CityAdapter
import com.m7.forecasto.util.Constants
import com.m7.forecasto.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreation(viewBinding: ActivitySearchBinding) {
        viewBinding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                searchViewModel.searchForCity(text.toString())
            }
        }

        searchViewModel.searchForCityData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { cities ->
                        viewBinding.rvResultCities.apply {
                            layoutManager = LinearLayoutManager(this@SearchActivity)
                            adapter = CityAdapter(
                                cities.takeIf { it.size >= 5 }?.subList(0, 5) ?: cities
                            ) {
                                startNext(CityForecastActivity::class.java, Bundle().apply {
                                    putParcelable(Constants.selectedCity, it)
                                })
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    displayError(it.msg ?: getString(it.resId ?: R.string.error_occurred))
                }
            }
        }
    }
}