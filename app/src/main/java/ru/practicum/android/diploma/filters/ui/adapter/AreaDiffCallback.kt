package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filters.domain.model.Area

// объект для страны и для региона
object AreaDiffCallback : DiffUtil.ItemCallback<Area>() {
    override fun areItemsTheSame(oldItem: Area, newItem: Area) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Area, newItem: Area) =
        oldItem == newItem
}
