package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.util.BindingFragment

class ChoiceIndustryFragment : BindingFragment<FragmentChoiceIndustryBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceIndustryBinding =
        FragmentChoiceIndustryBinding.inflate(inflater, container, false)
}
