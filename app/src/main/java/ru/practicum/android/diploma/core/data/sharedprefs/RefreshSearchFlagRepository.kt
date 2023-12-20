package ru.practicum.android.diploma.core.data.sharedprefs

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo

private const val REFRESH_SEARCH_KEY = "RefreshSearchKey"
class RefreshSearchFlagRepository(
    private val sharedPreferences: SharedPreferences
) : SaveDataRepo<Boolean>, GetDataRepo<Boolean> {

    override fun get(): Flow<Boolean?> = flow {
        emit(sharedPreferences.getBoolean(REFRESH_SEARCH_KEY, false))
        save(false)
    }

    override suspend fun save(data: Boolean?) {
        data?.run {
            sharedPreferences.edit().putBoolean(REFRESH_SEARCH_KEY, data).apply()
        }
    }

}
