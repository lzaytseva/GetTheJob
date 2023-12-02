package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentChoiceRegionBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceRegionViewModel
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class ChoiceRegionFragment : BindingFragment<FragmentChoiceRegionBinding>() {

    private val viewModel: ChoiceRegionViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceRegionBinding =
        FragmentChoiceRegionBinding.inflate(inflater, container, false)
}
