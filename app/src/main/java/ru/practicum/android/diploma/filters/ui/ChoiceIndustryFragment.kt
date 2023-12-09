package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.filters.presentation.ChoiceIndustryViewModel
import ru.practicum.android.diploma.util.BindingFragment

class ChoiceIndustryFragment : BindingFragment<FragmentChoiceIndustryBinding>() {

    private val viewModel: ChoiceIndustryViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChoiceIndustryBinding =
        FragmentChoiceIndustryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setIndustrySearchTextWatcher()
    }

    private fun setIndustrySearchTextWatcher() {
        binding.etSearchIndustry.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                binding.tilSearchIndustry.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
                binding.tilSearchIndustry.setEndIconOnClickListener {
                    binding.etSearchIndustry.text?.clear()
                }
            } else {
                binding.tilSearchIndustry.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
                binding.tilSearchIndustry.setEndIconOnClickListener {
                    // Поиск
                }
            }
        }
    }
}
