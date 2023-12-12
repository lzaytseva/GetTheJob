package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import ru.practicum.android.diploma.util.FeedbackUtils
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
        setLoadingNewPageObserver()
        configureToolbar()
        setupAdapter()
        setOnScrollListener()
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
            // Из hindeContent убрала скрытие recyclerView, т.к. из-за этого прогресс бар отображался поверх ресайклера
            // при загрузке новой страницы
            hideContent()
            binding.searchEditText.setText(screenState.state)
            when (screenState) {
                is SearchScreenState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.resultsListRecyclerView.isGone = true
                }

                is SearchScreenState.Error -> {
                    binding.resultsListRecyclerView.isGone = true
                    onError(screenState.error)
                }

                is SearchScreenState.Content -> {
                    binding.resultsListRecyclerView.isVisible = true
                    onContent(screenState.content)
                }

                is SearchScreenState.LoadingNextPage -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun setLoadingNewPageObserver() {
        viewModel.showLoadingNewPageError.observe(viewLifecycleOwner) {
            binding.progressBar.isGone = true
            when (it) {
                ErrorType.NO_INTERNET -> FeedbackUtils.showSnackbar(
                    requireView(),
                    getString(R.string.error_check_connection)
                )

                else -> FeedbackUtils.showSnackbar(
                    requireView(),
                    getString(R.string.error_something_went_wrong)
                )
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
            showSearchNotStarted()
        }
    }

    private fun showSearchNotStarted() {
        hideContent()
        with(binding) {
            resultsListRecyclerView.isGone = true
            searchImageView.isVisible = true
            searchImageView.setImageResource(R.drawable.ph_start_search)
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
            progressBar.isGone = true
        }
    }

    private fun onError(error: ErrorType) {
        binding.onErrorTextView.isVisible = true
        binding.searchImageView.isVisible = true
        when (error) {
            ErrorType.NO_INTERNET -> showError(R.drawable.ph_no_internet, R.string.error_no_internet)
            ErrorType.SERVER_ERROR -> showError(R.drawable.ph_server_error_search, R.string.error_server)
            ErrorType.NO_CONTENT -> showError(R.drawable.ph_nothing_found, R.string.error_getting_vacancies)
        }
    }

    private fun showError(imageResId: Int, stringResId: Int) {
        binding.searchImageView.setImageResource(imageResId)
        binding.onErrorTextView.text = getString(stringResId)
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

    private fun setOnScrollListener() {
        binding.resultsListRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        with(binding.resultsListRecyclerView) {
                            val pos =
                                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val itemsCount = adapter!!.itemCount
                            if (pos >= itemsCount - 1) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
        )
    }

    companion object {
        private const val ON_CLICK_DELAY = 200L
    }
}
