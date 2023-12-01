package ru.practicum.android.diploma.core.domain.api

import kotlinx.coroutines.flow.Flow

interface GetDataByIdRepo<T> {

    // nullable потому что при запросе из DB одного объекта есть вероятность получить null
    fun getById(id: Long): Flow<T?>
}
