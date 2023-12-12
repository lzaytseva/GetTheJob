package ru.practicum.android.diploma.vacancydetails.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.core.data.dto.requests.SimilarVacanciesSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.VacancySearchResponse
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.util.toVacancyInList
import ru.practicum.android.diploma.util.Resource

class SimilarVacanciesRepoImpl(
    private val networkClient: NetworkClient
) : GetDataByIdRepo<Resource<List<VacancyInList>>> {

    override fun getById(id: String): Flow<Resource<List<VacancyInList>>> = flow {
        val response = networkClient.doRequest(SimilarVacanciesSearchRequest(id))
        when (response.resultCode) {
            RetrofitNetworkClient.RC_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

            RetrofitNetworkClient.RC_OK -> emit(
                Resource.Success(
                    (response as VacancySearchResponse).dtos.map { dto ->
                        dto.toVacancyInList()
                    }
                )
            )

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
        }
    }.flowOn(Dispatchers.IO)
}
