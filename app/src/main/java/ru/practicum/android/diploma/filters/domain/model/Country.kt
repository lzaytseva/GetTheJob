package ru.practicum.android.diploma.filters.domain.model

data class Country(
    val id: String,
    val name: String,
    val parentId: String? = null
)
