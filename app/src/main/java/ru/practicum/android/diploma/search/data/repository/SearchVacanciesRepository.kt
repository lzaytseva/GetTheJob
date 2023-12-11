package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.util.toVacancyInList

class SearchVacanciesRepository(private val networkClient: NetworkClient)  : SearchRepo<VacancyInList> {

    override fun search(text: String): Flow<List<VacancyInList>?> = flow {
        // build VacanciesSearchRequest
        val request = VacanciesSearchRequest(text = text)
        val response = networkClient.doRequest(request)
        emit(
            if (response.resultCode == RetrofitNetworkClient.RC_OK) {
                (response as VacancySearchResponse).items.map { it.toVacancyInList() }
            } else {
                null
            }
        )
    }
}
