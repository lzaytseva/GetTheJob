package ru.practicum.android.diploma.vacancydetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.util.BindingFragment

class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSimilarVacanciesBinding =
        FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
}
