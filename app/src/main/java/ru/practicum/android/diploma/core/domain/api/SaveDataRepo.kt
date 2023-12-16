package ru.practicum.android.diploma.core.domain.api

interface SaveDataRepo<T> {

    suspend fun save(data: T?)
}
