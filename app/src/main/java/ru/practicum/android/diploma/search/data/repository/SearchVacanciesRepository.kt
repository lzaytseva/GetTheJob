package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.singleOrNull
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.util.toVacancyInList
import ru.practicum.android.diploma.util.Resource
import javax.inject.Named

class SearchVacanciesRepository(
    private val networkClient: NetworkClient,
    @Named(RepositoryModule.FILTERS_GET_REPOSITORY)
    private val getFiltersRepository: GetDataRepo<Filters>
) : SearchRepo<SearchResult> {

    override fun search(text: String, page: Int): Flow<Resource<SearchResult>> = flow {
        val filters = getFiltersRepository.get().singleOrNull()
        val request = buildRequest(filters, text, page)

        val response = networkClient.doRequest(request)

        emit(
            when (response.resultCode) {
                RetrofitNetworkClient.CODE_SUCCESS -> {
                    val vacancies = (response as VacancySearchResponse).items.map {
                        it.toVacancyInList()
                    }
                    val pages = response.pages
                    Resource.Success(
                        SearchResult(vacancies = vacancies, pages = pages, found = response.found)
                    )
                }

                RetrofitNetworkClient.CODE_NO_INTERNET -> {
                    Resource.Error(ErrorType.NO_INTERNET)
                }

                else -> {
                    Resource.Error(ErrorType.SERVER_ERROR)
                }
            }
        )
    }.flowOn(Dispatchers.IO)

    private fun buildRequest(filters: Filters?, text: String, page: Int): VacanciesSearchRequest =
        VacanciesSearchRequest(
            text = text,
            page = page,
            salary = filters?.salary,
            salaryFlag = filters?.salaryFlag,
            industryId = filters?.industryId,
            currency = filters?.currency,
            regionId = filters?.regionId ?: filters?.countryId
        )
}
