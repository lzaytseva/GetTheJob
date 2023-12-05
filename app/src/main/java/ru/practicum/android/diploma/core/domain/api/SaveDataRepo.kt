package ru.practicum.android.diploma.core.domain.api

interface SaveDataRepo<T> {

    fun save(data: T)
}
