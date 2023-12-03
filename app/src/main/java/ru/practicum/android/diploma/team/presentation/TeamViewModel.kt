package ru.practicum.android.diploma.team.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator
import ru.practicum.android.diploma.core.domain.models.EmailData
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val externalNavigator: ExternalNavigator,
) : ViewModel() {

    fun contactWithDeveloper(option: Developers) {
        externalNavigator.sendEmail(
            EmailData(
                emailAddress = when (option) {
                    Developers.DEV1 -> appContext.resources.getString(R.string.dev1_email)
                    Developers.DEV2 -> appContext.resources.getString(R.string.dev2_email)
                    Developers.DEV3 -> appContext.resources.getString(R.string.dev3_email)
                    Developers.DEV4 -> appContext.resources.getString(R.string.dev4_email)
                },
                subject = appContext.resources.getString(R.string.team_screen_email_subject),
                text = appContext.resources.getString(R.string.team_screen_email_text)
            )
        )
    }

    enum class Developers {
        DEV1, DEV2, DEV3, DEV4
    }
}
