package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.innerdto.AddressDto
import ru.practicum.android.diploma.core.data.dto.innerdto.ContactsDto
import ru.practicum.android.diploma.core.data.dto.innerdto.EmployerDto
import ru.practicum.android.diploma.core.data.dto.innerdto.LogoUrlsDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SalaryDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SkillDto
import ru.practicum.android.diploma.core.data.dto.innerdto.VacancyElementDto

data class VacancyDetailsDto(
    val id: String,
    val name: String,
    val area: VacancyElementDto,
    val salary: SalaryDto?,
    val experience: VacancyElementDto?,
    val schedule: VacancyElementDto,
    val contacts: ContactsDto?,
    val address: AddressDto?,
    val employer: EmployerDto?,
    val employment: VacancyElementDto?,
    @SerializedName("key_skills") val keySkills: List<SkillDto>?,
    val description: String
)
