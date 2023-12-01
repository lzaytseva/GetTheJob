package ru.practicum.android.diploma.core.domain.api

import kotlinx.coroutines.flow.Flow

interface GetDataRepo<T> {

    // nullable, потому что при запросе данных из sharedPreferences есть вероятность получить null
    fun get(): Flow<T?>
}
