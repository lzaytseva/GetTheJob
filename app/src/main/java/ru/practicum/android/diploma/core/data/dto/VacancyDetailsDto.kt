package ru.practicum.android.diploma.core.data.dto

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
    val salaryDto: SalaryDto?,
    val experience: VacancyElementDto?,
    val schedule: VacancyElementDto,
    val contactsDto: ContactsDto?,
    val logoUrlsDto: LogoUrlsDto?,
    val address: AddressDto?,
    val employer: EmployerDto?,
    val employment: VacancyElementDto?,
    val keySkills: List<SkillDto>
)
