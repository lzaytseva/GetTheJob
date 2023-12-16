package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryAdapter(
    private val onIndustryClickListener: (Industry, Int) -> Unit
) : ListAdapter<Industry, IndustryViewHolder>(IndustryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = getItem(position)

        holder.binding.checkBoxIndustry.setOnClickListener {
            onIndustryClickListener.invoke(industry, position)
        }

        holder.itemView.setOnClickListener {
            onIndustryClickListener.invoke(industry, position)
        }

        holder.bind(industry)
    }
}
