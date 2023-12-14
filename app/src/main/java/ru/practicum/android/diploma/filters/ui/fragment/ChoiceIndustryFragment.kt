package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.domain.model.IndustryScreenState
import ru.practicum.android.diploma.filters.presentation.ChoiceIndustryViewModel
import ru.practicum.android.diploma.filters.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

@AndroidEntryPoint
class ChoiceIndustryFragment : BindingFragment<FragmentChoiceIndustryBinding>() {

    private val viewModel: ChoiceIndustryViewModel by viewModels()
    private val adapter = IndustryAdapter { industry, position ->
        onIndustryClicked(industry, position)
    }
    private var lastSelectedIndustry: Industry? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceIndustryBinding =
        FragmentChoiceIndustryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setIndustrySearchTextWatcher()
        setEditorActionListener()
        initRecyclerView()
        configureToolbar()
    }

    override fun onResume() {
        super.onResume()
        val searchText = binding.etSearchIndustry.text
        if (searchText.isNullOrBlank()) {
            viewModel.getIndustries()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.Content -> showContent(state.industries)
            is IndustryScreenState.Error -> showError(state.error)
            is IndustryScreenState.Loading -> showLoading()
        }
    }

    private fun showContent(industries: List<Industry>) {
        hideFields(
            hideProgressBar = true,
            hideRecyclerView = false,
            hideErrorView = true
        )
        adapter.submitList(industries)
    }

    private fun showError(error: ErrorType) {
        hideFields(
            hideProgressBar = true,
            hideRecyclerView = true,
            hideErrorView = false
        )
        when (error) {
            ErrorType.NO_INTERNET -> {
                setErrorPlaceholders(
                    imageResId = R.drawable.ph_no_internet,
                    messageResId = R.string.error_no_internet
                )
            }

            ErrorType.SERVER_ERROR -> {
                setErrorPlaceholders(
                    imageResId = R.drawable.ph_server_error_search,
                    messageResId = R.string.error_server
                )
            }

            ErrorType.NO_CONTENT -> {
                setErrorPlaceholders(
                    imageResId = R.drawable.ph_nothing_found,
                    messageResId = R.string.error_no_such_industry
                )
            }
        }
    }

    private fun setErrorPlaceholders(imageResId: Int, messageResId: Int) {
        with(binding) {
            ivPlaceholderImage.setImageResource(imageResId)
            tvErrorMessage.setText(messageResId)
        }
    }

    private fun showLoading() {
        hideFields(
            hideProgressBar = false,
            hideRecyclerView = true,
            hideErrorView = true
        )
    }

    private fun hideFields(
        hideProgressBar: Boolean,
        hideRecyclerView: Boolean,
        hideErrorView: Boolean
    ) {
        with(binding) {
            rvIndustries.isGone = hideRecyclerView
            progressBar.isGone = hideProgressBar
            tvErrorMessage.isGone = hideErrorView
            ivPlaceholderImage.isGone = hideErrorView
        }
    }

    private fun setIndustrySearchTextWatcher() {
        binding.etSearchIndustry.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                search(text.toString())
                setEndIconClear()

            } else {
                setEndIconSearch()
                viewModel.getIndustries()
            }
        }
    }

    private fun search(text: String) {
        viewModel.search(text)
    }

    private fun setEndIconClear() {
        binding.tilSearchIndustry.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        binding.tilSearchIndustry.setEndIconOnClickListener {
            binding.etSearchIndustry.text?.clear()
            lastSelectedIndustry = null
            binding.btnSelect.isGone = true
            hideKeyboard()
        }
    }

    private fun setEndIconSearch() {
        binding.tilSearchIndustry.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
    }

    private fun hideKeyboard() {
        val keyboard =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.etSearchIndustry.windowToken, 0)
    }

    private fun setEditorActionListener() {
        binding.etSearchIndustry.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = binding.etSearchIndustry.text
                if (text != null) {
                    search(text.toString())
                }
                binding.etSearchIndustry.clearFocus()
                true
            }
            false
        }
    }

    private fun initRecyclerView() {
        binding.rvIndustries.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvIndustries.scrollToPosition(0)
            }
        })
        binding.rvIndustries.adapter = adapter
        binding.rvIndustries.itemAnimator = null
    }

    private fun onIndustryClicked(industry: Industry, position: Int) {
        val newList = adapter.currentList.toMutableList()
        if (industry.selected) {
            newList[position] = industry.copy(selected = false)
            lastSelectedIndustry = null
        } else {
            if (lastSelectedIndustry != null) {
                val lastSelectedIndex = newList.indexOf(lastSelectedIndustry)
                if (lastSelectedIndex != -1) {
                    newList[lastSelectedIndex] = newList[lastSelectedIndex].copy(selected = false)
                }
            }
            newList[position] = industry.copy(selected = true)
            lastSelectedIndustry = industry.copy(selected = true)
        }
        adapter.submitList(newList)
        binding.btnSelect.isGone = lastSelectedIndustry == null
    }


    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_industry)
        )
    }
}
