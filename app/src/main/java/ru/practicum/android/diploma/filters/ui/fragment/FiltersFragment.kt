package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.filters.presentation.state.FiltersScreenState
import ru.practicum.android.diploma.filters.ui.util.TextInputLayoutUtils
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

@AndroidEntryPoint
class FiltersFragment : BindingFragment<FragmentFiltersBinding>() {

    private val viewModel: FiltersViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltersBinding =
        FragmentFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFilters()
        observeViewModel()

        setBtnApplyClickListener()
        setBtnDiscardClickListener()
        configureSalaryField()
        configureFiltersFields()
        configureToolbar()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is FiltersScreenState.Content) {
                setFiltersValues(it.filters)
                updateButtonsVisibility()
            }
        }
    }

    private fun setFiltersValues(filters: Filters) {
        with (binding) {
            etIndustry.setText(filters.industryName)

        }
    }

    private fun setBtnApplyClickListener() {
        with(binding) {
            btnApplyChanges.setOnClickListener {
                // Сохранить настройки в SP
            }
        }
    }

    private fun setBtnDiscardClickListener() {
        binding.btnDiscardChanges.setOnClickListener {
            clearFields()
            setButtonsVisibility(isVisible = false)
            // И удалить настрйоки из sp
        }
    }

    private fun clearFields() {
        with(binding) {
            etSalary.text?.clear()
            etIndustry.text?.clear()
            etPlace.text?.clear()
            checkBoxSalary.isChecked = false
        }
    }

    private fun configureSalaryField() {
        setSalaryFieldHintColorBehaviour()
        setSalaryCheckBoxChangeListener()
        setSalaryTextWatcher()
        setEditorActionListener()
    }

    private fun configureFiltersFields() {
        setIndustryClickListener()
        setPlaceClickListener()
        setFiltersTextWatchers()
        setFiltersFieldsHintColorBehaviour()
        setFilterFieldsEndIcon()
    }

    private fun setSalaryFieldHintColorBehaviour() {
        val hintColorStateListId =
            if (!binding.etSalary.text.isNullOrBlank()) {
                R.color.salary_hint_color_populated
            } else {
                R.color.salary_hint_color
            }
        binding.tilSalary.defaultHintTextColor =
            ContextCompat.getColorStateList(requireContext(), hintColorStateListId)
    }

    private fun setSalaryCheckBoxChangeListener() {
        binding.checkBoxSalary.setOnCheckedChangeListener { buttonView, isChecked ->
            updateButtonsVisibility()
        }
    }

    private fun setSalaryTextWatcher() {
        binding.etSalary.doOnTextChanged { text, start, before, count ->
            updateButtonsVisibility()
            setSalaryFieldHintColorBehaviour()
        }
    }

    private fun setEditorActionListener() {
        binding.etSalary.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etSalary.clearFocus()
                true
            }
            false
        }
    }

    private fun setIndustryClickListener() {
        binding.etIndustry.setOnClickListener {
            findNavController().navigate(
                R.id.action_filtersFragment_to_choiceIndustryFragment
            )
        }
    }

    private fun setPlaceClickListener() {
        binding.etPlace.setOnClickListener {
            findNavController().navigate(
                R.id.action_filtersFragment_to_choiceWorkplaceFragment2
            )
        }
    }

    private fun setFiltersTextWatchers() {
        with(binding) {
            setFilterTextWatcher(tilIndustry, etIndustry)
            setFilterTextWatcher(tilPlace, etPlace)
        }
    }

    private fun setFilterTextWatcher(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText) {
        textInputEditText.doOnTextChanged { _, _, _, _ ->
            setFilterEndIcon(textInputLayout, textInputEditText)
            setFilterHintColor(textInputLayout, textInputEditText)
        }
    }

    private fun setFiltersFieldsHintColorBehaviour() {
        with(binding) {
            setFilterHintColor(tilIndustry, etIndustry)
            setFilterHintColor(tilPlace, etPlace)
        }
    }

    private fun setFilterHintColor(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText) {
        textInputLayout.defaultHintTextColor = TextInputLayoutUtils
            .getHintColorStateList(
                context = requireContext(),
                isTextFieldEmpty = textInputEditText.text.isNullOrBlank()
            )
    }

    private fun setFilterFieldsEndIcon() {
        with(binding) {
            setFilterEndIcon(tilIndustry, etIndustry)
            setFilterEndIcon(tilPlace, etPlace)
        }
    }

    private fun setFilterEndIcon(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText) {
        textInputLayout.endIconDrawable = TextInputLayoutUtils.getEndIconDrawable(
            context = requireContext(),
            iconResId = getEndIconId(isTextFieldEmpty = textInputEditText.text.isNullOrBlank())
        )
    }

    private fun getEndIconId(isTextFieldEmpty: Boolean): Int? {
        return if (isTextFieldEmpty) {
            R.drawable.ic_arrow_forward
        } else {
            null
        }
    }

    private fun updateButtonsVisibility() {
        val isVisible = hasNonEmptyFields()
        setButtonsVisibility(isVisible)
    }

    private fun setButtonsVisibility(isVisible: Boolean) {
        binding.btnApplyChanges.isGone = !isVisible
        binding.btnDiscardChanges.isGone = !isVisible
    }

    private fun hasNonEmptyFields(): Boolean {
        return with(binding) {
            !etIndustry.text.isNullOrBlank() ||
                !etPlace.text.isNullOrBlank() ||
                !etSalary.text.isNullOrBlank() ||
                checkBoxSalary.isChecked
        }
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_filter_settings)
        )
    }
}
