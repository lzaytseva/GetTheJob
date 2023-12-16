package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.ItemAreaBinding
import ru.practicum.android.diploma.filters.domain.model.Area

// класс для страны и для региона
class AreaAdapter(
    private val onCountryClick: (Area) -> Unit
) : ListAdapter<Area, AreaViewHolder>(AreaDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val binding = ItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AreaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val country = getItem(position)

        holder.itemView.setOnClickListener {
            onCountryClick.invoke(country)
        }

        holder.bind(country)
    }
}
