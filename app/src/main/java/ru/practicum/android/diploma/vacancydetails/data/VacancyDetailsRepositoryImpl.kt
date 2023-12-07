package ru.practicum.android.diploma.vacancydetails.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.VacancyDetailsRepository

private const val TAG = "VacancyDetailsRepositoryImpl"

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
) : VacancyDetailsRepository {

    override fun getVacancyDetailsById(vacancyId: String): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyDetailsSearchRequest(vacancyId))

        Log.d(TAG, "reusltCode = ${response.resultCode}")

        when (response.resultCode) {
            -1 -> emit(Resource.Error("No internet"))

            200 -> {
                emit(
                    with(response as VacancyDetailsSearchResponse) {
                        Resource.Success(VacancyDetailsDtoMapper.map(response.dto))
                    }
                )
            }

            else -> emit(Resource.Error("Server error"))
        }
    }.flowOn(Dispatchers.IO)
}
