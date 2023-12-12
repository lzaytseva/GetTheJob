package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.filters.ui.util.TextInputLayoutUtils
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

class FiltersFragment : BindingFragment<FragmentFiltersBinding>() {

    private val viewModel: FiltersViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltersBinding =
        FragmentFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSalaryFieldHintColorBehaviour()
        setSalaryCheckBoxChangeListener()
        setSalaryTextWatcher()
        setEditorActionListener()
        setIndustryClickListener()
        setPlaceClickListener()
        setFiltersFieldsHintColorBehaviour()
        setFilterFieldsEndIcon()
        setBtnsVisibility()
        configureToolbar()
        setBtnDiscardClickListener()
    }

    private fun setSalaryFieldHintColorBehaviour() {
        binding.etSalary.setOnFocusChangeListener { v, hasFocus ->
            val hintColorStateListId =
                if (!hasFocus && !binding.etSalary.text.isNullOrBlank()) {
                    R.color.salary_hint_color_populated
                } else {
                    R.color.salary_hint_color
                }
            binding.tilSalary.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), hintColorStateListId)
        }
    }

    private fun setSalaryCheckBoxChangeListener() {
        binding.checkBoxSalary.setOnCheckedChangeListener { buttonView, isChecked ->
            setBtnsVisibility()
        }
    }

    private fun setSalaryTextWatcher() {
        binding.etSalary.doOnTextChanged { text, start, before, count ->
            setBtnsVisibility()
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

    private fun setFilterFieldsEndIcon() {
        with(binding) {
            tilIndustry.endIconDrawable = TextInputLayoutUtils.getEndIconDrawable(
                context = requireContext(),
                iconResId = getEndIconId(isTextFieldEmpty = etIndustry.text.isNullOrBlank())
            )
            tilPlace.endIconDrawable = TextInputLayoutUtils.getEndIconDrawable(
                context = requireContext(),
                iconResId = getEndIconId(isTextFieldEmpty = etPlace.text.isNullOrBlank())
            )
        }
    }

    private fun getEndIconId(isTextFieldEmpty: Boolean): Int? {
        return if (isTextFieldEmpty) {
            R.drawable.ic_arrow_forward
        } else {
            null
        }
    }

    private fun setFiltersFieldsHintColorBehaviour() {
        with(binding) {
            tilIndustry.defaultHintTextColor = TextInputLayoutUtils
                .getHintColorStateList(
                    context = requireContext(),
                    isTextFieldEmpty = etIndustry.text.isNullOrBlank()
                )
            tilPlace.defaultHintTextColor = TextInputLayoutUtils
                .getHintColorStateList(
                    context = requireContext(),
                    isTextFieldEmpty = etPlace.text.isNullOrBlank()
                )
        }
    }


    private fun setBtnsVisibility() {
        val visible = hasNonEmptyFields()
        binding.btnApplyChanges.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.btnDiscardChanges.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun hasNonEmptyFields(): Boolean {
        return with(binding) {
            !etIndustry.text.isNullOrBlank() ||
                !etPlace.text.isNullOrBlank() ||
                !etSalary.text.isNullOrBlank() ||
                checkBoxSalary.isChecked
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

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_filter_settings)
        )
    }

    private fun setBtnDiscardClickListener() {
        with(binding) {
            btnDiscardChanges.setOnClickListener {
                etSalary.text?.clear()
                etIndustry.text?.clear()
                etPlace.text?.clear()
                checkBoxSalary.isChecked = false
                // И удалить настрйоки из sp
            }
        }
    }
}
