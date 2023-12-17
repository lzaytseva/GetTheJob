package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceRegionBinding
import ru.practicum.android.diploma.filters.presentation.state.ChoiceRegionScreenState
import ru.practicum.android.diploma.filters.presentation.viewmodel.ChoiceRegionViewModel
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
        setRegionSearchTextWatcher()
        setEditorActionListener()
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

    private fun setRegionSearchTextWatcher() {
        binding.etSearchRegion.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                search(text.toString())
                setEndIconClear()

            } else {
                setEndIconSearch()
                viewModel.getRegions()
            }
        }
    }

    private fun search(text: String) {
        viewModel.search(text)
    }

    private fun setEndIconClear() {
        binding.tilSearchRegion.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        binding.tilSearchRegion.setEndIconOnClickListener {
            binding.etSearchRegion.text?.clear()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val keyboard =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.etSearchRegion.windowToken, 0)
    }

    private fun setEndIconSearch() {
        binding.tilSearchRegion.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
    }

    private fun setEditorActionListener() {
        binding.etSearchRegion.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = binding.etSearchRegion.text
                if (text != null) {
                    search(text.toString())
                }
                binding.etSearchRegion.clearFocus()
                true
            }
            false
        }
    }
}
