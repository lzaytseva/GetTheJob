package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.databinding.FragmentChoiceCountryBinding
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.filters.presentation.ChoiceCountryScreenState
import ru.practicum.android.diploma.filters.presentation.ChoiceCountryViewModel
import ru.practicum.android.diploma.filters.ui.adapter.CountryAdapter
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

@AndroidEntryPoint
class ChoiceCountryFragment : BindingFragment<FragmentChoiceCountryBinding>() {

    private val viewModel: ChoiceCountryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceCountryBinding =
        FragmentChoiceCountryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

        setFilterTextWatcher()

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is ChoiceCountryScreenState.Error -> showError(screenState.message)
                is ChoiceCountryScreenState.Loading -> showLoading()
                is ChoiceCountryScreenState.Content -> showContent(screenState.countries)
            }
        }
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_country)
        )
    }

    private fun showError(error: ErrorType?) {
        when (error) {
            ErrorType.NO_INTERNET -> {
                with(binding) {
                    placeholder.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    countries.visibility = View.GONE
                    placeholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ph_no_internet)
                    )
                    placeholderMessage.text = getString(R.string.error_no_internet)
                }
            }

            else -> {
                with(binding) {
                    placeholder.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    countries.visibility = View.GONE
                    placeholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ph_error_get_list)
                    )
                    placeholderMessage.text = getString(R.string.error_getting_list)
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            placeholder.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            countries.visibility = View.GONE
        }
    }

    private fun showContent(countries: List<Country>) {
        binding.placeholder.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.countries.visibility = View.VISIBLE
        val adapter = CountryAdapter(onCountryClick = { })
        binding.countries.adapter = adapter
        adapter.submitList(countries)

    }

    private fun setFilterTextWatcher() {
        binding.etSearchCountry.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                viewModel.filterCountries(text.toString())
                setEndIconClear()

            } else {
                setEndIconSearch()
                viewModel.getCountries()
            }
        }
    }

    private fun setEndIconClear() {
        binding.tilSearchCountry.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
        binding.tilSearchCountry.setEndIconOnClickListener {
            binding.etSearchCountry.text?.clear()
            hideKeyboard()
        }
    }

    private fun setEndIconSearch() {
        binding.tilSearchCountry.endIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
        binding.tilSearchCountry.setEndIconOnClickListener {
            binding.etSearchCountry.text?.clear()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val keyboard =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.etSearchCountry.windowToken, 0)
    }

}
