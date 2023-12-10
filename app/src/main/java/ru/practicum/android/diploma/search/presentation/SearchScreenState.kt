package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacancyInList

sealed class SearchScreenState {
    var state: String = ""

    data class Loading(val queryState: String) : SearchScreenState()
    data class Error(val error: ErrorType, val queryState: String) : SearchScreenState()
    data class Content(val content: List<VacancyInList>, val queryState: String) : SearchScreenState()
}
