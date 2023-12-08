package ru.practicum.android.diploma.vacancydetails.data

import ru.practicum.android.diploma.core.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.core.domain.models.VacancyDetails

object VacancyDetailsDtoMapper {

    fun map(dto: VacancyDetailsDto): VacancyDetails =
        VacancyDetails(
            id = dto.id,
            name = dto.name,
            area = dto.area.name,
            salaryCurrency = dto.salary?.currency,
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            salaryGross = dto.salary?.gross,
            experience = dto.experience?.name,
            schedule = dto.schedule.name,
            contactName = dto.contacts?.name,
            contactEmail = dto.contacts?.email,
            phones = dto.contacts?.phones?.map { phone ->
                "+${phone.country}${phone.city}${phone.number}"
            },
            contactComment = dto.contacts?.phones?.get(0)?.comment,
            logoUrl = dto.employer?.logoUrlsDto?.original,
            logoUrl90 = dto.employer?.logoUrlsDto?.art90,
            logoUrl240 = dto.employer?.logoUrlsDto?.art240,
            address = "${dto.address?.city}, ${dto.address?.street}, ${dto.address?.building}",
            employerUrl = dto.employer?.alternateUrl,
            employerName = dto.employer?.name,
            employment = dto.employment?.name,
            keySkills = dto.keySkills?.map { skillDto ->
                skillDto.name
            },
            description = dto.description
        )

}
