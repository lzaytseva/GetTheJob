package ru.practicum.android.diploma.core.domain.api

interface StoreDataRepo<T> {

    fun store(data: T)
}
