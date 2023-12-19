package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filters.domain.model.Industry

object IndustryDiffCallback : DiffUtil.ItemCallback<Industry>() {
    override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem == newItem
    }
}
