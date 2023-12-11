package ru.practicum.android.diploma.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

private const val SCALE_ANIM_DURATION = 250L
private const val SCALE_ANIM_START_VALUE = 1f
private const val SCALE_ANIM_END_VALUE = 1.1f


fun scaleAnimation(view: View) {
    val animIncrease = ScaleAnimation(
        SCALE_ANIM_START_VALUE,
        SCALE_ANIM_END_VALUE,
        SCALE_ANIM_START_VALUE,
        SCALE_ANIM_END_VALUE,
        Animation.RELATIVE_TO_SELF,
        SCALE_ANIM_START_VALUE,
        Animation.RELATIVE_TO_SELF,
        SCALE_ANIM_START_VALUE
    ).apply {
        fillAfter = true
        duration = SCALE_ANIM_DURATION
    }

    val animDecrease = ScaleAnimation(
        SCALE_ANIM_END_VALUE,
        SCALE_ANIM_START_VALUE,
        SCALE_ANIM_END_VALUE,
        SCALE_ANIM_START_VALUE,
        Animation.RELATIVE_TO_SELF,
        SCALE_ANIM_START_VALUE,
        Animation.RELATIVE_TO_SELF,
        SCALE_ANIM_START_VALUE
    ).apply {
        fillAfter = true
        duration = SCALE_ANIM_DURATION
    }

    view.startAnimation(animIncrease)
    view.startAnimation(animDecrease)
}
