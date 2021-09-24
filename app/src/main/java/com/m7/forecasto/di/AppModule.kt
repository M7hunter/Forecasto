package com.m7.forecasto.di

import com.m7.forecasto.BuildConfig
import com.m7.forecasto.data.api.ForecastAPIs
import com.m7.forecasto.data.api.ForecastAPIsImpl
import com.m7.forecasto.data.api.SuggestionAPIs
import com.m7.forecasto.data.api.SuggestionAPIsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideForecastAPIsImpl(apis: ForecastAPIs): ForecastAPIsImpl = ForecastAPIsImpl(apis)

    @Provides
    fun provideSuggestionAPIsImpl(apis: SuggestionAPIs): SuggestionAPIsImpl = SuggestionAPIsImpl(apis)

    @Provides
    fun provideForecastAPIs(okHttpClient: OkHttpClient): ForecastAPIs = Retrofit.Builder()
        .baseUrl(BuildConfig.FORECAST_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ForecastAPIs::class.java)

    @Provides
    fun provideSuggestionAPIs(okHttpClient: OkHttpClient): SuggestionAPIs = Retrofit.Builder()
        .baseUrl(BuildConfig.PLACES_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SuggestionAPIs::class.java)

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG)
                addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
        }.build()
}