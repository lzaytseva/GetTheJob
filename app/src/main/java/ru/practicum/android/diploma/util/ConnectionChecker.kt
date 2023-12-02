package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectionChecker(context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
