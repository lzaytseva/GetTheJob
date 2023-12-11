package ru.practicum.android.diploma.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

fun scaleAnimation(view: View) {
    val animIncrease = ScaleAnimation(
        1f, 1.1f,
        1f, 1.1f,
        Animation.RELATIVE_TO_SELF, 1f,
        Animation.RELATIVE_TO_SELF, 1f
    ).apply {
        fillAfter = true
        duration = 250
    }

    val animDecrease = ScaleAnimation(
        1.1f, 1f,
        1.1f, 1f,
        Animation.RELATIVE_TO_SELF, 1f,
        Animation.RELATIVE_TO_SELF, 1f
    ).apply {
        fillAfter = true
        duration = 250
    }

    view.startAnimation(animIncrease)
    view.startAnimation(animDecrease)
}
