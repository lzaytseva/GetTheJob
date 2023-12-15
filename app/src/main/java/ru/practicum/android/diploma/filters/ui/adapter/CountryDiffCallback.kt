package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filters.domain.model.Country

object CountryDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Country, newItem: Country) =
        oldItem == newItem
}
