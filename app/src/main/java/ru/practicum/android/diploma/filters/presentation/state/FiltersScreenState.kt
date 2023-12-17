package ru.practicum.android.diploma.filters.presentation.state

import ru.practicum.android.diploma.core.domain.models.Filters

sealed interface FiltersScreenState {
    data class Content(val filters: Filters) : FiltersScreenState
}
