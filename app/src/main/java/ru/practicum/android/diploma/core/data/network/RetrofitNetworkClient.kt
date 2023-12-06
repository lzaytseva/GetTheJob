package ru.practicum.android.diploma.core.data.network

import android.content.Context
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.Response
import ru.practicum.android.diploma.util.ConnectionChecker

class RetrofitNetworkClient(
    private val context: Context,
    private val hhService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = RC_NO_INTERNET }
        }

        return when (request) {
            is VacancyDetailsSearchRequest -> {
                try {
                    val response = hhService.getVacancyDetailsById(request.id)
                    response.apply { resultCode = RC_OK }
                } catch (t: Throwable) {
                    Response().apply { resultCode = RC_NOK }
                }
            }

            else -> {
                Response().apply { resultCode = RC_NOK_SERVER_ERROR }
            }
        }
    }

    companion object {
        const val RC_NO_INTERNET = -1
        const val RC_OK = 200 //TODO replace later
        const val RC_NOK = 500 //TODO replace later
        const val RC_NOK_SERVER_ERROR = 500 //TODO replace later
    }
}
