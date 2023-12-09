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
        holder.bind(industry)

        val onClickListener = OnClickListener {
            if (industry.selected) {
                currentList[position].selected = false
            } else {
                if (lastSelected != null) {
                    val lastSelectedIndex = currentList.indexOf(lastSelected)
                    if (lastSelectedIndex != -1) {
                        currentList[lastSelectedIndex].selected = false
                    }
                    notifyItemChanged(lastSelectedIndex)
                }
                currentList[position].selected = true
                lastSelected = industry
            }
            notifyItemChanged(position)
        }

        holder.binding.checkBoxIndustry.setOnClickListener(onClickListener)
        holder.itemView.setOnClickListener(onClickListener)
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
