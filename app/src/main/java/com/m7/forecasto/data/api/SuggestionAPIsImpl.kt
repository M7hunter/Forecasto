package com.m7.forecasto.data.api

import com.m7.forecasto.BuildConfig
import javax.inject.Inject

class SuggestionAPIsImpl @Inject constructor(private val apis: SuggestionAPIs) {

    suspend fun getSuggestions(
        text: String,
        gApiToken: String = BuildConfig.GOOGLE_API_KEY,
        type: String = "textquery",
        fields: String = "formatted_address,name"
    ) = apis.getSuggestions(text, gApiToken, type, fields)
}