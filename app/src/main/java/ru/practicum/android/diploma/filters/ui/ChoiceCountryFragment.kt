package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentChoiceCountryBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceCountryViewModel
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class ChoiceCountryFragment : BindingFragment<FragmentChoiceCountryBinding>() {

    private val viewModel: ChoiceCountryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceCountryBinding =
        FragmentChoiceCountryBinding.inflate(inflater, container, false)
}
