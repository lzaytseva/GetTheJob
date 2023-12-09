package ru.practicum.android.diploma.util

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity

object ToolbarUtils {
    fun configureToolbar(activity: FragmentActivity, navController: NavController, title: String) {
        val toolbar = (activity as RootActivity).toolbar
        with(toolbar) {
            this.title = title
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                navController.navigateUp()
            }
            hideMenu()
        }

    }

    private fun Toolbar.hideMenu() {
        menu.findItem(R.id.favorite).isVisible = false
        menu.findItem(R.id.share).isVisible = false
        menu.findItem(R.id.filters).isVisible = false
    }
}
