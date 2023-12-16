package ru.practicum.android.diploma.filters.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryAdapter(
    private val onIndustryClickListener: (Industry, Int) -> Unit
) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {
    var industries = mutableListOf<Industry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding)
    }

    override fun getItemCount(): Int = industries.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = industries[holder.layoutPosition]
        Log.d("INDUSTRY", "onBindViewHolder for position: $position")
        holder.binding.checkBoxIndustry.setOnClickListener {
            Log.d("INDUSTRY", "Inside checked changed")
            onIndustryClickListener.invoke(industry, holder.layoutPosition)
        }

        holder.itemView.setOnClickListener {
            Log.d("INDUSTRY", "inside clickListener")
            onIndustryClickListener.invoke(industry, holder.layoutPosition)
        }

        holder.bind(industry)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads.first() == true) {
                holder.bindSelected(industries[holder.layoutPosition].selected)
            }
        }
    }

    class IndustryViewHolder(val binding: ItemIndustryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(industry: Industry) {
            with(binding) {
                tvIndustryName.text = industry.name
                checkBoxIndustry.isChecked = industry.selected
            }
        }

        fun bindSelected(isSelected: Boolean) {
            binding.checkBoxIndustry.isChecked = isSelected
            Log.d("INDUSTRY", "bindSelected for: ${this.layoutPosition}")
        }
    }
}
