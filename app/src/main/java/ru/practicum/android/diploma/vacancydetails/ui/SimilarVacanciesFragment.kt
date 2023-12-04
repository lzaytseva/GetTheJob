package ru.practicum.android.diploma.vacancydetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesViewModel

@AndroidEntryPoint
class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel: SimilarVacanciesViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSimilarVacanciesBinding =
        FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
}
