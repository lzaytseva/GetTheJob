package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.util.toVacancyInList
import ru.practicum.android.diploma.util.Resource

class SearchVacanciesRepository(private val networkClient: NetworkClient) : SearchRepo<SearchResult> {

    override fun search(text: String, page: Int): Flow<Resource<SearchResult>> = flow {
        // build VacanciesSearchRequest
        val request = VacanciesSearchRequest(text = text)
        val response = networkClient.doRequest(request)
        emit(
            when (response.resultCode) {
                RetrofitNetworkClient.RC_OK -> {
                    val vacancies = (response as VacancySearchResponse).items.map {
                        it.toVacancyInList()
                    }
                    val pages = response.pages
                    Resource.Success(
                        SearchResult(vacancies, pages = pages, found = response.found)
                    )
                }

                RetrofitNetworkClient.RC_NO_INTERNET -> {
                    Resource.Error(ErrorType.NO_INTERNET)
                }

                else -> {
                    Resource.Error(ErrorType.SERVER_ERROR)
                }
            }
        )
    }

}
