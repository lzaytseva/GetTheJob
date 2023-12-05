package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FiltersFragment : BindingFragment<FragmentFiltersBinding>() {

    private val viewModel: FiltersViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltersBinding =
        FragmentFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSalaryFieldHintColorBehaviour()
        setSalaryCheckBoxChangeListener()
        setSalaryTextWatcher()
    }

    override fun onResume() {
        super.onResume()
        setFilterFieldsEndIcon()
        setBtnsVisibility()
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

    private fun setFilterFieldsEndIcon() {
        with(binding) {
            tilIndustry.endIconDrawable = if (etIndustry.text.isNullOrEmpty()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_forward)
            } else {
                null
            }
            tilPlace.endIconDrawable = if (etPlace.text.isNullOrEmpty()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_forward)
            } else {
                null
            }
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
}
