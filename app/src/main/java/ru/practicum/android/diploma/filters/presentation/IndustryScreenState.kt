package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Industry

sealed interface IndustryScreenState {
    data object Loading : IndustryScreenState

    data class Content(val industries: List<Industry>) : IndustryScreenState

    data class Error(val error: ErrorType) : IndustryScreenState
}
