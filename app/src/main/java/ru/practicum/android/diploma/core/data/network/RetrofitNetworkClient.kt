package ru.practicum.android.diploma.core.data.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.Response
import ru.practicum.android.diploma.core.data.dto.responses.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.util.ConnectionChecker

private const val TAG = "RetrofitNetworkClient"

class RetrofitNetworkClient(
    private val context: Context,
    private val hhService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = RC_NO_INTERNET }
        }

        return when (request) {
            is VacancyDetailsSearchRequest -> getVacancyDetailsById(request.id)
            is VacanciesSearchRequest -> {
//                getVacanciesList(request.toQueryMap())
                Response().apply { resultCode = RC_NOK_SERVER_ERROR }
            }
            else -> Response().apply { resultCode = RC_NOK_SERVER_ERROR }

        }
    }

    private suspend fun getVacancyDetailsById(id: String): Response {
        return try {
            val response = hhService.getVacancyDetailsById(id)
            if (response.code() == RC_OK && response.body() != null) {
                VacancyDetailsSearchResponse(response.body()!!)
                    .apply { resultCode = RC_OK }
            } else {
                Response().apply { resultCode = RC_NOK }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = RC_NOK }
        }
    }

//    private suspend fun getVacanciesList(queryMap: Map<String, String>) = withContext(Dispatchers.IO) {
//        try {
//            Resource.Success(hhService.getVacancies(queryMap))
//        } catch (_: Exception) {
//            Resource.Error("$RC_NOK_SERVER_ERROR")
//        }
//    }

    companion object {
        const val RC_NO_INTERNET = -1
        const val RC_OK = 200
        const val RC_NOK = 500
        const val RC_NOK_SERVER_ERROR = 500
    }
}
