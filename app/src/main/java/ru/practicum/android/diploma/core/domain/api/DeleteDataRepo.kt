package ru.practicum.android.diploma.core.domain.api

interface DeleteDataRepo<T> {

    suspend fun delete(data: T)
}
