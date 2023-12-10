package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
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
        setIndustryClickListener()
        setPlaceClickListener()
        configureToolbar()
    }

    override fun onResume() {
        super.onResume()
        setFiltersFieldsHintColorBehaviour()
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

    private fun setFiltersFieldsHintColorBehaviour() {
        with(binding) {
            tilIndustry.setHintColor(
                hintColorStateListId = getHintColorStateListId(isTextFieldEmpty = etIndustry.text.isNullOrBlank())
            )
            tilPlace.setHintColor(
                hintColorStateListId = getHintColorStateListId(isTextFieldEmpty = etPlace.text.isNullOrBlank())
            )
        }
    }

    private fun getHintColorStateListId(isTextFieldEmpty: Boolean): Int {
        return if (isTextFieldEmpty) {
            R.color.filter_hint_color
        } else {
            R.color.filter_hint_color_populated
        }
    }

    private fun TextInputLayout.setHintColor(hintColorStateListId: Int) {
        defaultHintTextColor = ContextCompat.getColorStateList(requireContext(), hintColorStateListId)
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
            title = getString(R.string.header_industry)
        )
    }
}
