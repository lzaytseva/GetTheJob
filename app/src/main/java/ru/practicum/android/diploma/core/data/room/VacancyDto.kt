package ru.practicum.android.diploma.core.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class VacancyDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val vacancyId: String,
    val name: String,
//    val area: AreaDto,
//    val salary: Salary?,
//    val experience: Experience?,
//    val schedule: Schedule,
//    val contacts: Contacts?
)
