package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class FiltersFragment : BindingFragment<FragmentFiltersBinding>() {

    private val viewModel: FiltersViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltersBinding =
        FragmentFiltersBinding.inflate(inflater, container, false)
}
