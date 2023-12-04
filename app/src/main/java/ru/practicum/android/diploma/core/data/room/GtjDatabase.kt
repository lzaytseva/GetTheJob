package ru.practicum.android.diploma.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.data.room.entity.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 0,
    exportSchema = false
)
abstract class GtjDatabase : RoomDatabase() {

    abstract val vacancyDao: VacancyDao
}
