package ru.practicum.android.diploma.filters.presentation.state

sealed interface FiltersScreenState {
    data class Settings(
        val workPlace: String,
        val industry: String,
        val salary: String,
        val onlyWithSalary: Boolean,
    ) : FiltersScreenState
}
