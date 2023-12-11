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
import ru.practicum.android.diploma.core.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class SimilarVacanciesRepoImpl(
    private val networkClient: NetworkClient
) : GetDataByIdRepo<Resource<List<Vacancy>>> {

    override fun getById(id: String): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(SimilarVacanciesSearchRequest(id))
        when (response.resultCode) {
            RetrofitNetworkClient.RC_NO_INTERNET -> emit(Resource.Error("No internet"))

            RetrofitNetworkClient.RC_OK -> emit(
                Resource.Success(
                    (response as VacancySearchResponse).dtos.map { dto ->
                        Vacancy( // add mapping when search will be ready
                            id = dto.id,
                            name = dto.name,
                            area = dto.area.name,
                            salaryCurrency = dto.salary?.currency,
                            salaryFrom = dto.salary?.from,
                            salaryTo = dto.salary?.to,
                            logoUrl90 = dto.employer?.logoUrlsDto?.art90,
                            address = null,
                            employerName = dto.employer?.name,
                        )
                    }
                )
            )

            else -> emit(Resource.Error("Server error"))
        }
    }.flowOn(Dispatchers.IO)
}
