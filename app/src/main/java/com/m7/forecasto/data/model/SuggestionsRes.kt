package com.m7.forecasto.data.model

data class SuggestionsRes(
    val status: String,
    val error_message: String?,
    val results: List<Suggestion>?,
)
