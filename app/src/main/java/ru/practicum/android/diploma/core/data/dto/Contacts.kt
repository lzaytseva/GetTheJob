package ru.practicum.android.diploma.core.data.dto

data class Contacts(
    val email: String,
    val name: String,
    val phones: List<Phone>
)
