package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceWorkplaceBinding
import ru.practicum.android.diploma.filters.presentation.state.ChoiceWorkplaceScreenState
import ru.practicum.android.diploma.filters.presentation.viewmodel.ChoiceWorkplaceViewModel
import ru.practicum.android.diploma.filters.ui.util.TextInputLayoutUtils
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

@AndroidEntryPoint
class ChoiceWorkplaceFragment : BindingFragment<FragmentChoiceWorkplaceBinding>() {

    private val viewModel: ChoiceWorkplaceViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceWorkplaceBinding =
        FragmentChoiceWorkplaceBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        setFieldsTextWatchers()
        configureCountryField()
        configureRegionField()
        setBtnSelectClickListener()

        binding.etCountry.setOnClickListener {
            val action = ChoiceWorkplaceFragmentDirections.actionChoiceWorkplaceFragmentToChoiceCountryFragment()
            findNavController().navigate(action)
        }

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            showFiltersFields(state)
            setBtnSelectVisible(state.isBtnSelectVisible)
        }

        binding.etRegion.setOnClickListener {
            val action = ChoiceWorkplaceFragmentDirections.actionChoiceWorkplaceFragmentToChoiceRegionFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilters()
    }

    private fun configureToolbar() {
        ToolbarUtils.configureToolbar(
            activity = requireActivity(),
            navController = findNavController(),
            title = getString(R.string.header_workplace)
        )
    }

    private fun setFieldsTextWatchers() {
        binding.etCountry.doOnTextChanged { _, _, _, _ ->
            configureCountryField()
            updateBtnSelectVisibility()
        }
        binding.etRegion.doOnTextChanged { _, _, _, _ ->
            configureRegionField()
            updateBtnSelectVisibility()
        }
    }

    private fun configureCountryField() {
        setFieldEndIcon(
            textInputLayout = binding.tilCountry,
            editText = binding.etCountry
        )
        setFieldHintColorBehaviour(
            textInputLayout = binding.tilCountry,
            editText = binding.etCountry
        )
        setEndIconListeners(
            textInputLayout = binding.tilCountry,
            editText = binding.etCountry,
            actionId = R.id.action_choiceWorkplaceFragment_to_choiceCountryFragment
        )
    }

    private fun configureRegionField() {
        setFieldEndIcon(
            textInputLayout = binding.tilRegion,
            editText = binding.etRegion
        )
        setFieldHintColorBehaviour(
            textInputLayout = binding.tilRegion,
            editText = binding.etRegion
        )
        setEndIconListeners(
            textInputLayout = binding.tilRegion,
            editText = binding.etRegion,
            actionId = R.id.action_choiceWorkplaceFragment_to_choiceRegionFragment
        )
    }

    private fun setFieldHintColorBehaviour(textInputLayout: TextInputLayout, editText: TextInputEditText) {
        textInputLayout.defaultHintTextColor = TextInputLayoutUtils
            .getHintColorStateList(
                context = requireContext(),
                isTextFieldEmpty = editText.text.isNullOrBlank()
            )
    }

    private fun setFieldEndIcon(textInputLayout: TextInputLayout, editText: TextInputEditText) {
        textInputLayout.endIconDrawable = TextInputLayoutUtils.getEndIconDrawable(
            context = requireContext(),
            iconResId = getEndIconId(isTextFieldEmpty = editText.text.isNullOrBlank())
        )
    }

    private fun getEndIconId(isTextFieldEmpty: Boolean): Int {
        return if (isTextFieldEmpty) {
            R.drawable.ic_arrow_forward
        } else {
            R.drawable.ic_clear
        }
    }

    private fun setEndIconListeners(
        textInputLayout: TextInputLayout,
        editText: TextInputEditText,
        actionId: Int
    ) {
        if (editText.text.isNullOrBlank()) {
            textInputLayout.setEndIconOnClickListener {
                findNavController().navigate(
                    actionId
                )
            }
        } else {
            textInputLayout.setEndIconOnClickListener {
                editText.text?.clear()
                if (textInputLayout == binding.tilCountry) {
                    binding.etRegion.text?.clear()
                    viewModel.deleteCountryRegion()
                } else {
                    viewModel.deleteRegion()
                }
            }
        }
    }

    private fun showFiltersFields(screenState: ChoiceWorkplaceScreenState) {
        if (!screenState.country.isNullOrBlank()) {
            binding.etCountry.setText(screenState.country, TextView.BufferType.EDITABLE)
            binding.etRegion.setText(screenState.region, TextView.BufferType.EDITABLE)
        }
    }

    private fun setBtnSelectVisible(isVisible: Boolean) {
        binding.btnSelect.isGone = !isVisible
    }

    private fun setBtnSelectClickListener() {
        binding.btnSelect.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateBtnSelectVisibility() {
        if (binding.etRegion.text.isNullOrBlank() && binding.etCountry.text.isNullOrBlank()) {
            binding.btnSelect.isGone = true
        }
    }
}
