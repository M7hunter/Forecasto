package com.m7.forecasto.util.handlers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityHandler @Inject constructor(@ApplicationContext val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val networkCaps = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork ?: return false
        ) ?: return false

        return when {
            networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }
}