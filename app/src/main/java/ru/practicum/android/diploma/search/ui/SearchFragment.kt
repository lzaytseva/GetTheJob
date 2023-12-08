package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.presentation.SearchScreenState
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private var searchFieldState: Boolean = false
        private set(value) {
            if (field != value) {
                field = value
                changeIcon(value)
            }
        }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureSearchField()
        setObserver()
        configureToolbar()
    }

    override fun onResume() {
        super.onResume()
        setFiltersVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        setFiltersVisibility(false)
    }

    private fun setObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            hideContent()
            when (screenState) {
                is SearchScreenState.Loading -> binding.progressBar.isVisible = true
                is SearchScreenState.Error -> onError(screenState.error)
                is SearchScreenState.Content -> onContent(screenState.content)
            }
        }
    }

    private fun configureSearchField() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            text?.toString()?.run {
                searchFieldState = this.isNotBlank()
                viewModel::search
            }
        }
    }

    private fun changeIcon(flag: Boolean) {
        val image = if (flag) R.drawable.ic_clear else R.drawable.ic_search
        binding.searchFieldImageView.setImageResource(image)
    }

    private fun hideContent() {
        with(binding) {
            searchImageView.isGone = true
            resultMessageTextView.isGone = true
            onErrorTextView.isGone = true
            resultsListRecyclerView.isGone = true
            progressBar.isGone = true
        }
    }

    private fun onError(error: ErrorType) {
        when (error) {
            ErrorType.NO_INTERNET -> {}
            ErrorType.SERVER_ERROR -> {}
            ErrorType.NO_CONTENT -> {}
        }
    }

    private fun onContent(content: ArrayList<VacancyInList>) {
        val adapter = binding.resultsListRecyclerView.adapter as? VacanciesAdapter
        adapter?.setContent(content)
    }

    private fun setFiltersVisibility(isVisible: Boolean) {
        (requireActivity() as? RootActivity)?.run {
            toolbar.menu.findItem(R.id.filters)?.isVisible = isVisible
        }
    }

    private fun configureToolbar() {
        (requireActivity() as? RootActivity)?.run {
            toolbar.title = ContextCompat.getString(this@run, R.string.search_screen)
            val item = toolbar.menu.findItem(R.id.filters)
            item.isVisible = true
            item.setOnMenuItemClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filtersFragment)
                true
            }
        }
    }
}
