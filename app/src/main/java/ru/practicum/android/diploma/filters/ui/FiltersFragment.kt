package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.util.BindingFragment

class FiltersFragment : BindingFragment<FragmentFiltersBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltersBinding =
        FragmentFiltersBinding.inflate(inflater, container, false)
}
