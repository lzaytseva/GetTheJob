package ru.practicum.android.diploma.core.data.dto.innerdto

data class ContactsDto(
    val email: String?,
    val name: String?,
    val phones: List<PhoneDto>?,
)
