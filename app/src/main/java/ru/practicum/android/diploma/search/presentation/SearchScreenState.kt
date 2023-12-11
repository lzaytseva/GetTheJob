package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacancyInList

sealed class SearchScreenState {
    var state: String = ""

    data object Loading : SearchScreenState()
    data class Error(val error: ErrorType) : SearchScreenState()
    data class Content(val content: List<VacancyInList>) : SearchScreenState()
    data object LoadingNextPage : SearchScreenState()
}
