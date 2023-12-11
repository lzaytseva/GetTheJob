package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
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
import ru.practicum.android.diploma.util.debounce

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private var searchIsNotEmpty: Boolean = false
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
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        setFiltersVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        setFiltersVisibility(false)
        viewModel.saveQueryState(binding.searchEditText.text.toString())
    }

    private fun setObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            hideContent()
            binding.searchEditText.setText(screenState.state)
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
                searchIsNotEmpty = this.isNotBlank()
                binding.searchFieldImageView.isEnabled = searchIsNotEmpty
                if (searchIsNotEmpty) viewModel.search(this)
            }
        }
        val inputMethodManager = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)

        binding.searchFieldImageView.setOnClickListener {
            binding.searchEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
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
            ErrorType.NO_INTERNET -> { Toast.makeText(requireContext(), "NoInternet", Toast.LENGTH_SHORT).show() }
            ErrorType.SERVER_ERROR -> { Toast.makeText(requireContext(), "ServerError", Toast.LENGTH_SHORT).show() }
            ErrorType.NO_CONTENT -> { Toast.makeText(requireContext(), "NoContent", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun onContent(content: List<VacancyInList>) {
        val adapter = binding.resultsListRecyclerView.adapter as? VacanciesAdapter
        adapter?.setContent(content)
        binding.resultsListRecyclerView.isVisible = true
        binding.resultMessageTextView.isVisible = true
        binding.resultMessageTextView.text = getString(R.string.search) // get plurals for correct message
    }

    private fun setupAdapter() {
        val onVacancyClick: (String) -> Unit = debounce(ON_CLICK_DELAY, lifecycleScope, false) { vacancyId ->
            val searchToDetails: NavDirections =
                SearchFragmentDirections.actionSearchFragmentToVacancyDetailsFragment(vacancyId)
            findNavController().navigate(searchToDetails)
        }
        binding.resultsListRecyclerView.adapter = VacanciesAdapter(onVacancyClick)
    }

    private fun setFiltersVisibility(isVisible: Boolean) {
        (requireActivity() as? RootActivity)?.run {
            toolbar.menu.findItem(R.id.filters)?.isVisible = isVisible
        }
    }

    private fun configureToolbar() {
        (requireActivity() as? RootActivity)?.run {
            toolbar.title = getString(R.string.search_screen)
            val item = toolbar.menu.findItem(R.id.filters)
            item.isVisible = true
            item.setOnMenuItemClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filtersFragment)
                true
            }
        }
    }

    companion object {
        private const val ON_CLICK_DELAY = 200L
    }
}
