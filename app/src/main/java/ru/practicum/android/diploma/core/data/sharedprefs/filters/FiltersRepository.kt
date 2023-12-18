package ru.practicum.android.diploma.core.data.sharedprefs.filters

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters

class FiltersRepository(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SaveDataRepo<Filters>, GetDataRepo<Filters> {

    override fun get(): Flow<Filters?> = flow {
        sharedPreferences.getString(FILTERS_KEY, null)?.let { filtersJson ->
            emit(gson.fromJson(filtersJson, Filters::class.java))
        } ?: emit(null)
    }

    override suspend fun save(data: Filters?) {
        val filtersJson = gson.toJson(data, Filters::class.java)
        sharedPreferences.edit().putString(FILTERS_KEY, filtersJson).apply()
    }

    companion object {
        private const val FILTERS_KEY = "FiltersKey"
    }
}
