package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryViewHolder(val binding: ItemIndustryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(industry: Industry) {
        with(binding) {
            tvIndustryName.text = industry.name
            checkBoxIndustry.isChecked = industry.selected
        }
    }
}
