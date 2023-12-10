package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesViewModel

class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel: SimilarVacanciesViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSimilarVacanciesBinding =
        FragmentSimilarVacanciesBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

    }

    private fun configureToolbar() {
        val toolbar = (requireActivity() as RootActivity).toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        toolbar.setTitle(getString(R.string.vacancy_similar_vacancies))
        toolbar.menu.findItem(R.id.favorite).isVisible = false
        toolbar.menu.findItem(R.id.share).isVisible = false
        toolbar.menu.findItem(R.id.filters).isVisible = false

    }
}



