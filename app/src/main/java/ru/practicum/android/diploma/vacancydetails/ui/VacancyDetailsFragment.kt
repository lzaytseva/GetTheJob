package ru.practicum.android.diploma.vacancydetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsViewModel

@AndroidEntryPoint
class VacancyDetailsFragment : BindingFragment<FragmentVacancyDetailsBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyDetailsBinding =
        FragmentVacancyDetailsBinding.inflate(inflater, container, false)
}
