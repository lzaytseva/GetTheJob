package ru.practicum.android.diploma.core.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator

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


}
