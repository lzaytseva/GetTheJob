package ru.practicum.android.diploma.filters.ui.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import ru.practicum.android.diploma.R

object TextInputLayoutUtils {
    private fun getHintColorStateListId(isTextFieldEmpty: Boolean): Int {
        return if (isTextFieldEmpty) {
            R.color.filter_hint_color
        } else {
            R.color.filter_hint_color_populated
        }
    }

    fun getHintColorStateList(context: Context, isTextFieldEmpty: Boolean): ColorStateList {
        return ContextCompat.getColorStateList(
            context,
            getHintColorStateListId(isTextFieldEmpty)
        )!!
    }

    fun getEndIconDrawable(context: Context, iconResId: Int?): Drawable? {
        return if (iconResId == null) {
            null
        } else {
            ContextCompat.getDrawable(context, iconResId)
        }
    }
}
