package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.domain.model.Country

private const val OTHER_COUNTRIES = "Другие регионы"

class CountryAdapter(
    private val onCountryClick: (Country) -> Unit
) : ListAdapter<Country, CountryViewHolder>(CountryDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)

        holder.itemView.setOnClickListener {
            onCountryClick.invoke(country)
        }

        holder.bind(country)
    }

    override fun submitList(list: List<Country>?) {
        val muList = list?.toMutableList()
        val lastElement = muList?.find { country -> country.name == OTHER_COUNTRIES }
        if (lastElement != null) {
            muList.remove(lastElement)
            muList.add(lastElement)
        }
        super.submitList(muList)
    }
}
