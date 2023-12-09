package ru.practicum.android.diploma.filters.ui.fragment

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        setFiltersFieldsHintColorBehaviour()
        setFilterFieldsEndIcon()
        setBtnsVisibility()
        configureToolbar()
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
            tilIndustry.endIconDrawable = getEndIconDrawable(
                iconResId = getEndIconId(isTextFieldEmpty = etIndustry.text.isNullOrBlank())
            )
            tilPlace.endIconDrawable = getEndIconDrawable(
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

    private fun getEndIconDrawable(iconResId: Int?): Drawable? {
        return if (iconResId == null) {
            null
        } else {
            ContextCompat.getDrawable(requireContext(), iconResId)
        }
    }

    private fun setFiltersFieldsHintColorBehaviour() {
        with(binding) {
            tilIndustry.defaultHintTextColor = getHintColorStateList(
                hintColorStateListId = getHintColorStateListId(
                    isTextFieldEmpty = etIndustry.text.isNullOrBlank()
                )
            )
            tilPlace.defaultHintTextColor = getHintColorStateList(
                hintColorStateListId = getHintColorStateListId(
                    isTextFieldEmpty = etPlace.text.isNullOrBlank()
                )
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

    private fun getHintColorStateList(hintColorStateListId: Int): ColorStateList {
        return ContextCompat.getColorStateList(requireContext(), hintColorStateListId)!!
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
