package ru.practicum.android.diploma.core.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.util.Resource

interface SearchRepo<T> {

    fun search(text: String, page: Int): Flow<Resource<T>>
}
