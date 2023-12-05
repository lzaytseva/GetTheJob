package ru.practicum.android.diploma.core.domain.api

import kotlinx.coroutines.flow.Flow

interface SearchRepo<T> {

    fun search(query: String): Flow<List<T>>
}
