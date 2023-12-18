package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.Vacancy

sealed class SearchScreenState {

    data object Loading : SearchScreenState()
    data class Error(val error: ErrorType) : SearchScreenState()
    data class Content(val content: List<Vacancy>, val found: Int) : SearchScreenState()
    data object LoadingNextPage : SearchScreenState()
    data class LoadingNextPageError(val error: ErrorType) : SearchScreenState()
}
