package ru.practicum.android.diploma.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao

@Database(
//    entities = [VacancyDto::class],
    version = 0
)
abstract class GtjDatabase : RoomDatabase() {

    abstract val vacancyDao: VacancyDao
}
