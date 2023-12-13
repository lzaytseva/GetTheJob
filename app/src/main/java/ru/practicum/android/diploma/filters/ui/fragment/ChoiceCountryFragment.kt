package ru.practicum.android.diploma.filters.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentChoiceCountryBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceCountryViewModel
import ru.practicum.android.diploma.util.BindingFragment

class ChoiceCountryFragment : BindingFragment<FragmentChoiceCountryBinding>() {

    private val viewModel: ChoiceCountryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceCountryBinding =
        FragmentChoiceCountryBinding.inflate(inflater, container, false)
}
