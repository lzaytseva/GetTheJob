package ru.practicum.android.diploma.filters.domain.model

import ru.practicum.android.diploma.core.domain.models.ErrorType

sealed interface IndustryScreenState {
    data object Loading : IndustryScreenState
    data class Content(val industries: List<Industry>) : IndustryScreenState

    data class Error(val error: ErrorType) : IndustryScreenState

    data object Empty : IndustryScreenState
}
