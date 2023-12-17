package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Country

sealed interface ChoiceCountryScreenState {
    data class Error(val message: ErrorType?) : ChoiceCountryScreenState

    object Loading : ChoiceCountryScreenState

    data class Content(val countries: List<Country>, val searchEnabled:Boolean = false) : ChoiceCountryScreenState
}
