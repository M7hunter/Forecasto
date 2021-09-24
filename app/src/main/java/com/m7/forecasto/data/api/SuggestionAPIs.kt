package com.m7.forecasto.data.api

import com.m7.forecasto.data.model.SuggestionsRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SuggestionAPIs {

    @GET("textsearch/json")
    suspend fun getSuggestions(
        @Query("input") text: String,
        @Query("key") gApiToken: String,
        @Query("inputtype") type: String = "textquery",
        @Query("fields") fields: String = "formatted_address,name"
    ): Response<SuggestionsRes>
}