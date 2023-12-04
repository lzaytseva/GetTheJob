package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceIndustryViewModel
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class ChoiceIndustryFragment : BindingFragment<FragmentChoiceIndustryBinding>() {

    private val viewModel: ChoiceIndustryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceIndustryBinding =
        FragmentChoiceIndustryBinding.inflate(inflater, container, false)
}
