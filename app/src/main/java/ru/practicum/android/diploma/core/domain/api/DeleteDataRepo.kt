package ru.practicum.android.diploma.core.domain.api

interface DeleteDataRepo<T> {

    fun delete(data: T)
}
