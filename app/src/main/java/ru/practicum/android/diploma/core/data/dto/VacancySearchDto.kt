package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.innerdto.EmployerDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SalaryDto

data class VacancySearchDto(
    val id: String,
    val name: String,
    @SerializedName("salary")
    val salaryDto: SalaryDto?,
    @SerializedName("employer")
    val employerDto: EmployerDto
)
