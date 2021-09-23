package com.m7.forecasto.di

import android.content.Context
import com.m7.forecasto.data.db.CityDao
import com.m7.forecasto.data.db.ForecastoDb
import com.m7.forecasto.data.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    fun provideWeatherDao(db: ForecastoDb): WeatherDao = db.weatherDao()

    @Provides
    fun provideCityDao(db: ForecastoDb): CityDao = db.cityDao()

    @Provides
    fun provideForecastoDb(@ApplicationContext context: Context): ForecastoDb =
        ForecastoDb.getInstance(context)
}