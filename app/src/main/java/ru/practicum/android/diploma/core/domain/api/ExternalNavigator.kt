package ru.practicum.android.diploma.core.domain.api

import ru.practicum.android.diploma.core.domain.models.EmailData

interface ExternalNavigator {

    fun openUrlLink(link: String)

    fun sendEmail(emailData: EmailData)

    fun share(link: String)
}
