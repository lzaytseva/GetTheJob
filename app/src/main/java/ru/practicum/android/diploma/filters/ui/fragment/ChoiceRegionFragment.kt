package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceRegionBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceRegionScreenState
import ru.practicum.android.diploma.filters.presentation.ChoiceRegionViewModel
import ru.practicum.android.diploma.filters.ui.adapter.CountryAdapter
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

@AndroidEntryPoint
class ChoiceRegionFragment : BindingFragment<FragmentChoiceRegionBinding>() {

    private val viewModel: ChoiceRegionViewModel by viewModels()
    private var adapter: CountryAdapter? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceRegionBinding =
        FragmentChoiceRegionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        viewModel.state.observe(viewLifecycleOwner) { renderState(it) }
        adapter = CountryAdapter { item ->
            viewModel.clickItem(item)
            findNavController().navigateUp()
        }
        binding.rvRegions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvRegions.adapter = adapter
    }

    override fun onDestroyView() {
        binding.rvRegions.adapter = null
        super.onDestroyView()
        adapter = null
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_region)
        )
    }

    private fun renderState(state: ChoiceRegionScreenState) {
        when (state) {
            is ChoiceRegionScreenState.Content -> renderContent(state)
            ChoiceRegionScreenState.Empty -> renderEmpty()
            ChoiceRegionScreenState.Error -> renderError()
            ChoiceRegionScreenState.Loading -> renderLoading()
        }
    }

    private fun renderLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvRegions.visibility = View.GONE
        binding.groupEmpty.visibility = View.GONE
        binding.groupError.visibility = View.GONE
    }

    private fun renderError() {
        binding.progressBar.visibility = View.GONE
        binding.rvRegions.visibility = View.GONE
        binding.groupEmpty.visibility = View.GONE
        binding.groupError.visibility = View.VISIBLE
    }

    private fun renderEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.rvRegions.visibility = View.GONE
        binding.groupEmpty.visibility = View.VISIBLE
        binding.groupError.visibility = View.GONE
    }

    private fun renderContent(state: ChoiceRegionScreenState.Content) {
        binding.progressBar.visibility = View.GONE
        binding.rvRegions.visibility = View.VISIBLE
        binding.groupEmpty.visibility = View.GONE
        binding.groupError.visibility = View.GONE
        adapter?.submitList(state.regions)
    }
}
