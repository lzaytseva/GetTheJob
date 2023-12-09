package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentChoiceCountryBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceCountryViewModel
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

class ChoiceCountryFragment : BindingFragment<FragmentChoiceCountryBinding>() {

    private val viewModel: ChoiceCountryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceCountryBinding =
        FragmentChoiceCountryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_industry)
        )
    }
}
