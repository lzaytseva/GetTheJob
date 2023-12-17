package ru.practicum.android.diploma.filters.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceWorkplaceBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceWorkplaceViewModel
import ru.practicum.android.diploma.filters.ui.util.TextInputLayoutUtils
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.ToolbarUtils

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

        binding.etCountry.setOnClickListener {
            val action = ChoiceWorkplaceFragmentDirections.actionChoiceWorkplaceFragmentToChoiceCountryFragment()
            findNavController().navigate(action)
        }

        binding.etRegion.setOnClickListener {
            val action = ChoiceWorkplaceFragmentDirections.actionChoiceWorkplaceFragmentToChoiceRegionFragment()
            findNavController().navigate(action)
        }
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
        }
        binding.etRegion.doOnTextChanged { _, _, _, _ ->
            configureRegionField()
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
            }
        }
    }
}
