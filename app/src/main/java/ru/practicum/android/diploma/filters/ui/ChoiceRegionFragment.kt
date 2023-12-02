package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentChoiceRegionBinding
import ru.practicum.android.diploma.util.BindingFragment

class ChoiceRegionFragment : BindingFragment<FragmentChoiceRegionBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceRegionBinding =
        FragmentChoiceRegionBinding.inflate(inflater, container, false)
}
