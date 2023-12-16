package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemAreaBinding
import ru.practicum.android.diploma.filters.domain.model.Area

// класс для страны и для региона
class AreaViewHolder(val binding: ItemAreaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(area: Area) {
        binding.areaName.text = area.name
    }
}
