package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.VacancyInList

class SearchVacanciesRepository : SearchRepo<VacancyInList> {

    override fun search(query: String): Flow<List<VacancyInList>> = flow {
        emit(emptyList())
    }
}
