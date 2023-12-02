package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentChoiceWorkplaceBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceWorkplaceViewModel
import ru.practicum.android.diploma.util.BindingFragment

class ChoiceWorkplaceFragment : BindingFragment<FragmentChoiceWorkplaceBinding>() {

    private val viewModel: ChoiceWorkplaceViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceWorkplaceBinding =
        FragmentChoiceWorkplaceBinding.inflate(inflater, container, false)
}
