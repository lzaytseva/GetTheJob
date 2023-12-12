package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.ui.SearchFragment
import ru.practicum.android.diploma.search.ui.SearchFragmentDirections
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesScreenState
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesViewModel

@AndroidEntryPoint
class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel: SimilarVacanciesViewModel by viewModels()
    private val toolbar by lazy { (requireActivity() as RootActivity).toolbar }
    private val adapter: VacanciesAdapter? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSimilarVacanciesBinding =
        FragmentSimilarVacanciesBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

        viewModel.similarVacanciesScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SimilarVacanciesScreenState.Loading -> showLoading()
                is SimilarVacanciesScreenState.Content -> showContent(state.vacancies)
                is SimilarVacanciesScreenState.Error -> showError()

            }

        }

    }

    private fun showContent(vacancies: List<VacancyInList>) {
        setupAdapter()
        adapter?.setContent(vacancies)
    }

    private fun showLoading() {

    }

    private fun showError() {

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

    private fun setupAdapter() {
        val onVacancyClick: (String) -> Unit =
            debounce(ON_CLICK_DELAY, lifecycleScope, false) { vacancyId ->
                val searchToDetails: NavDirections =
                    SearchFragmentDirections.actionSearchFragmentToVacancyDetailsFragment(vacancyId)
                findNavController().navigate(searchToDetails)
            }
        binding.similarVacancies.adapter = VacanciesAdapter(onVacancyClick)
    }

    companion object {
        private const val ON_CLICK_DELAY = 200L
    }
}



