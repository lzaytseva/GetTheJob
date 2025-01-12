package ru.practicum.android.diploma.vacancydetails.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.core.data.dto.requests.SimilarVacanciesSearchRequest
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.util.toVacancyInList
import ru.practicum.android.diploma.util.Resource

class SimilarVacanciesRepoImpl(
    private val networkClient: NetworkClient
) : GetDataByIdRepo<Resource<List<Vacancy>>> {

    override fun getById(id: String): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(SimilarVacanciesSearchRequest(id))
        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

            RetrofitNetworkClient.CODE_SUCCESS -> emit(
                Resource.Success(
                    (response as VacancySearchResponse).items.map { dto ->
                        dto.toVacancyInList()
                    }
                )
            )

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
        }
    }.flowOn(Dispatchers.IO)
}
