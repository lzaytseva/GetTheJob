package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesScreenState
import ru.practicum.android.diploma.vacancydetails.presentation.SimilarVacanciesViewModel

@AndroidEntryPoint
class SimilarVacanciesFragment : BindingFragment<FragmentSimilarVacanciesBinding>() {

    private val viewModel: SimilarVacanciesViewModel by viewModels()
    private val toolbar by lazy { (requireActivity() as RootActivity).toolbar }
    private var adapter: VacanciesAdapter? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSimilarVacanciesBinding =
        FragmentSimilarVacanciesBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        viewModel.similarVacanciesScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SimilarVacanciesScreenState.Loading -> showLoading()
                is SimilarVacanciesScreenState.Content -> showContent(state.vacancies)
                is SimilarVacanciesScreenState.Error -> showError(state.message)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun showContent(vacancies: List<Vacancy>) {
        binding.progressBar.visibility = View.GONE
        binding.errorImage.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.similarVacancies.visibility = View.VISIBLE
        adapter?.setContent(vacancies)
    }

    private fun showLoading() {
        binding.errorImage.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.similarVacancies.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(error: ErrorType?) {
        binding.errorImage.visibility = View.VISIBLE
        binding.errorMessage.visibility = View.VISIBLE
        binding.similarVacancies.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        when (error) {
            ErrorType.NO_INTERNET -> {
                binding.errorImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ph_no_internet
                    )
                )
                binding.errorMessage.text = getString(R.string.error_no_internet)
            }

            else -> {
                binding.errorImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ph_server_error_search
                    )
                )
                binding.errorMessage.text = getString(R.string.error_server)
            }
        }
    }

    private fun configureToolbar() {
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
                    SimilarVacanciesFragmentDirections.actionSimilarVacanciesFragmentToVacancyDetailsFragment(vacancyId)
                findNavController().navigate(searchToDetails)
            }
        adapter = VacanciesAdapter(onVacancyClick)
        binding.similarVacancies.adapter = adapter
    }

    companion object {
        private const val ON_CLICK_DELAY = 200L
    }
}



