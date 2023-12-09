package ru.practicum.android.diploma.core.data.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator
import ru.practicum.android.diploma.core.domain.models.EmailData

class ExternalNavigatorImpl(
    @ApplicationContext private val appContext: Context
) : ExternalNavigator {
    override fun openUrlLink(link: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        startActivity(appContext, intent, null)
    }

    override fun sendEmail(emailData: EmailData) {
        val sendEmailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse(appContext.getString(R.string.uri_mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        startActivity(appContext, sendEmailIntent, null)
    }

    override fun share(link: String) {
        val shareLinkIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = appContext.getString(R.string.text_plain)
            putExtra(Intent.EXTRA_TEXT, link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        startActivity(appContext, shareLinkIntent, null)
    }

    override fun makePhoneCall(number: String) {
        val phoneCallIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse(appContext.getString(R.string.tel, number))
        }
        val chooser = Intent.createChooser(phoneCallIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(appContext, chooser, null)
    }
}
