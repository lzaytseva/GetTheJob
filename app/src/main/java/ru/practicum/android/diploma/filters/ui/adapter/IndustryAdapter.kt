package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryAdapter : ListAdapter<Industry, IndustryAdapter.IndustryViewHolder>(IndustryDiffCallback) {
    private var lastSelected: Industry? = null

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
        val newList = currentList.toMutableList()

        val onClickListener = OnClickListener {
            if (industry.selected) {
                newList[position] = industry.copy(selected = false)
                lastSelected = null
            } else {
                if (lastSelected != null) {
                    val lastSelectedIndex = newList.indexOf(lastSelected)
                    if (lastSelectedIndex != -1) {
                        newList[lastSelectedIndex] = newList[lastSelectedIndex].copy(selected = false)
                    }
                }
                newList[position] = industry.copy(selected = true)
                lastSelected = industry
            }
            submitList(newList)
        }

        holder.binding.checkBoxIndustry.setOnClickListener(onClickListener)
        holder.itemView.setOnClickListener(onClickListener)
        holder.bind(industry)
    }

    class IndustryViewHolder(val binding: ItemIndustryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(industry: Industry) {
            with(binding) {
                tvIndustryName.text = industry.name
                checkBoxIndustry.isChecked = industry.selected
            }
        }
    }
}
