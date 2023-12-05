package ru.practicum.android.diploma.vacancydetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsViewModel

class VacancyDetailsFragment : BindingFragment<FragmentVacancyDetailsBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyDetailsBinding =
        FragmentVacancyDetailsBinding.inflate(inflater, container, false)
}
