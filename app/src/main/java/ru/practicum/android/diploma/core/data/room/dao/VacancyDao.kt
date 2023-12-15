package ru.practicum.android.diploma.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.data.room.entity.VacancyEntity
import ru.practicum.android.diploma.core.data.room.entity.VacancyShort

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVacancy(entity: VacancyEntity)

    @Query("DELETE FROM favorites_table WHERE vacancyId = :vacancyId")
    suspend fun deleteVacancy(vacancyId: String)

    @Query("SELECT vacancyId, name, salaryCurrency, salaryFrom, salaryTo, employerName, logoUrl FROM favorites_table")
    fun getVacancyList(): Flow<List<VacancyShort>>

    @Query("SELECT * FROM favorites_table WHERE vacancyId = :vacancyId LIMIT 1")
    suspend fun getVacancyById(vacancyId: String): VacancyEntity?

    @Update
    suspend fun updateVacancy(entity: VacancyEntity)
}
