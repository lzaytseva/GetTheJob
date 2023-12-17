package ru.practicum.android.diploma.filters.ui.adapter

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
        val industry = industries[position]
        holder.binding.checkBoxIndustry.setOnClickListener {
            onIndustryClickListener.invoke(industry, position)
        }

        holder.itemView.setOnClickListener {
            onIndustryClickListener.invoke(industry, position)
        }

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
