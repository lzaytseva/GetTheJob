package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.domain.model.Country

class CountryViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(country: Country) {
        binding.countryName.text = country.name
    }
}
