package ru.practicum.android.diploma.core.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.dto.requests.SimilarVacanciesSearchRequest
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.Response
import ru.practicum.android.diploma.core.data.dto.responses.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.filters.data.dto.AllAreasRequest
import ru.practicum.android.diploma.filters.data.dto.AllAreasResponse
import ru.practicum.android.diploma.filters.data.dto.CountriesRequest
import ru.practicum.android.diploma.filters.data.dto.CountriesResponse
import ru.practicum.android.diploma.filters.data.dto.CountryByIdRequest
import ru.practicum.android.diploma.filters.data.dto.CountryByIdResponse
import ru.practicum.android.diploma.filters.data.dto.IndustriesRequest
import ru.practicum.android.diploma.filters.data.dto.IndustriesResponse
import ru.practicum.android.diploma.search.util.toQueryMap
import ru.practicum.android.diploma.util.ConnectionChecker

class RetrofitNetworkClient(
    private val context: Context,
    private val hhService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = CODE_NO_INTERNET }
        }

        return withContext(Dispatchers.IO) {
            when (request) {
                is VacancyDetailsSearchRequest -> getVacancyDetailsById(request.id)
                is IndustriesRequest -> getIndustries()
                is VacanciesSearchRequest -> getVacanciesList(request.toQueryMap())
                is SimilarVacanciesSearchRequest -> getSimilarVacanciesById(request.id)
                is CountriesRequest -> getCountries()
                is AllAreasRequest -> getAllAreas()
                is CountryByIdRequest -> getRegionsById(request.id)
                else -> Response().apply { resultCode = CODE_WRONG_REQUEST }
            }
        }
    }

    private suspend fun getVacancyDetailsById(id: String): Response {
        return try {
            val response = hhService.getVacancyDetailsById(id)
            if (response.code() == CODE_SUCCESS && response.body() != null) {
                VacancyDetailsSearchResponse(response.body()!!).apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getVacanciesList(queryMap: Map<String, String>): Response = withContext(Dispatchers.IO) {
        try {
            with(hhService.getVacancies(queryMap)) {
                if (code() == CODE_SUCCESS && body() != null) {
                    body()!!.apply { resultCode = CODE_SUCCESS }
                } else {
                    Response().apply { resultCode = CODE_SERVER_ERROR }
                }
            }
        } catch (_: Exception) {
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getIndustries(): Response {
        return try {
            val response = hhService.getIndustries()
            if (response.body() != null) {
                IndustriesResponse(response.body()!!).apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getSimilarVacanciesById(id: String): Response {
        return try {
            val response = hhService.getSimilarVacanciesById(id)
            if (response.code() == CODE_SUCCESS && response.body() != null) {
                response.body()!!.apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getCountries(): Response {
        return try {
            val response = hhService.getCountries()
            if (response.body() != null) {
                CountriesResponse(response.body()!!).apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getAllAreas(): Response {
        return try {
            val response = hhService.getAllAreas()
            if (response.body() != null) {
                AllAreasResponse(response.body()!!).apply { resultCode = CODE_SUCCESS }
            } else {
                Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getRegionsById(id: String): Response {
        return try {
            val response = hhService.getAreaById(id)
            when {
                response.body() == null -> Response().apply { resultCode = CODE_SERVER_ERROR }
                response.body()?.parentId != null -> getRegionsById(response.body()!!.parentId!!)
                response.body()?.parentId == null -> CountryByIdResponse(response.body()!!).apply {
                    resultCode = CODE_SUCCESS
                }

                else -> Response().apply { resultCode = CODE_SERVER_ERROR }
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.toString())
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    companion object {
        const val CODE_NO_INTERNET = -1
        const val CODE_SUCCESS = 200
        const val CODE_WRONG_REQUEST = 400
        const val CODE_SERVER_ERROR = 500
        private const val TAG = "RetrofitNetworkClient"
    }
}
