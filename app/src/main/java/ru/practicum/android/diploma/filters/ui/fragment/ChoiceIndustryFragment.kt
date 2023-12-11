package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter = IndustryAdapter()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceIndustryBinding =
        FragmentChoiceIndustryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setIndustrySearchTextWatcher()
        initRecyclerView()
        configureToolbar()
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
            is IndustryScreenState.Empty -> showEmpty()
            is IndustryScreenState.Loading -> showLoading()
        }
    }

    private fun showContent(industries: List<Industry>) {
        adapter.submitList(industries)
    }

    private fun showError(error: ErrorType) {
        TODO("Not implemented yet")
    }

    private fun showEmpty() {
        TODO("Not implemented yet")
    }

    private fun showLoading() {
        TODO("Not implemented yet")
    }

    private fun setIndustrySearchTextWatcher() {
        binding.etSearchIndustry.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                binding.tilSearchIndustry.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
                binding.tilSearchIndustry.setEndIconOnClickListener {
                    binding.etSearchIndustry.text?.clear()
                }
            } else {
                binding.tilSearchIndustry.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
                binding.tilSearchIndustry.setEndIconOnClickListener {
                    // Поиск
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvIndustries.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvIndustries.adapter = adapter
        binding.rvIndustries.itemAnimator = null
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_industry)
        )
    }
}
