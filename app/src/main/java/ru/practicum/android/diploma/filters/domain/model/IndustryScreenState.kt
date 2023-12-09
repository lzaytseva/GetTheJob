package ru.practicum.android.diploma.filters.domain.model

sealed interface IndustryScreenState {
    data object Loading: IndustryScreenState
    data class Content(val industries: List<Industry>): IndustryScreenState

    data class Error(val message: String): IndustryScreenState

    data object Empty: IndustryScreenState
}
