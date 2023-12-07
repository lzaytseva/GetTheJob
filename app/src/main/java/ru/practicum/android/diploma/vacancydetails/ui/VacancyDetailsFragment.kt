package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsScreenState
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsViewModel

@AndroidEntryPoint
class VacancyDetailsFragment : BindingFragment<FragmentVacancyDetailsBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyDetailsBinding =
        FragmentVacancyDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.vacancyDetailsScreenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is VacancyDetailsScreenState.Content -> showContent(screenState.vacancyDetails)
                is VacancyDetailsScreenState.Loading -> showLoading()
                is VacancyDetailsScreenState.Error -> showError()
            }
        }
    }

    private fun showContent(vacancyDetails: VacancyDetails) {
        binding.loading.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.content.visibility = View.VISIBLE
        binding.positionName.text = vacancyDetails.name
    }

    private fun showLoading() {
        binding.content.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.loading.visibility = View.GONE
        binding.content.visibility = View.GONE
        binding.error.visibility = View.VISIBLE
    }

}


