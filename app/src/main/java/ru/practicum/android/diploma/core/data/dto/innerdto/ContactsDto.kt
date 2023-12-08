package ru.practicum.android.diploma.core.data.dto.innerdto

data class ContactsDto(
    val name: String?,
    val email: String?,
    val phoneDtos: List<PhoneDto>?,
)
