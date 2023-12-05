package ru.practicum.android.diploma.core.data.network

import android.content.Context
import ru.practicum.android.diploma.core.data.dto.Response
import ru.practicum.android.diploma.util.ConnectionChecker

class RetrofitNetworkClient(
    private val context: Context,
    private val hhService: HhApiService
) : NetworkClient {

    override fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = RC_NO_INTERNET }
        }
        return Response()
    }

    companion object {
        const val RC_NO_INTERNET = -1
    }
}
