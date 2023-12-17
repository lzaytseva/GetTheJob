package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Country

sealed interface ChoiceRegionScreenState {
    data class Error(val error: ErrorType) : ChoiceRegionScreenState
    data object Loading : ChoiceRegionScreenState
    data object Empty : ChoiceRegionScreenState
    data class Content(val regions: List<Country>) : ChoiceRegionScreenState
}
