package ru.practicum.android.diploma.vacancydetails.data

import ru.practicum.android.diploma.core.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.core.domain.models.VacancyDetails

object VacancyDetailsDtoMapper {

    fun map(dto: VacancyDetailsDto): VacancyDetails =
        VacancyDetails(
            id = dto.id,
            name = dto.name,
            area = dto.area.name,
            salaryCurrency = dto.salaryDto?.currency,
            salaryFrom = dto.salaryDto?.from,
            salaryTo = dto.salaryDto?.to,
            salaryGross = dto.salaryDto?.gross,
            experience = dto.experience?.name,
            schedule = dto.schedule.name,
            contactName = dto.contactsDto?.name,
            contactEmail = dto.contactsDto?.email,
            phones = dto.contactsDto?.phoneDtos?.map { phoneDto ->
                phoneDto.country + phoneDto.city + phoneDto.number
            },
            logoUrl = dto.logoUrlsDto?.original,
            logoUrl90 = dto.logoUrlsDto?.art90,
            logoUrl240 = dto.logoUrlsDto?.art240,
            address = dto.address?.city + dto.address?.street + dto.address?.building,
            employerUrl = dto.employer?.alternateUrl,
            employerName = dto.employer?.name,
            employment = dto.employment?.name,
            keySkills = dto.keySkills?.map { skillDto ->
                skillDto.name
            },
            description = dto.description
        )

}
