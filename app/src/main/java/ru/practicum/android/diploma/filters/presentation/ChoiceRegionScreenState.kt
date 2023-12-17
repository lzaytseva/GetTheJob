package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.filters.domain.model.Country

sealed interface ChoiceRegionScreenState {
    data object Error : ChoiceRegionScreenState
    data object Loading : ChoiceRegionScreenState
    data object Empty : ChoiceRegionScreenState
    data class Content(val regions: List<Country>) : ChoiceRegionScreenState
}
