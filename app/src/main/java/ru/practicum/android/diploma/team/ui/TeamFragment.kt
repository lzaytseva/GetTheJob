package ru.practicum.android.diploma.team.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.team.presentation.TeamViewModel
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class TeamFragment : BindingFragment<FragmentTeamBinding>() {

    private val viewModel: TeamViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTeamBinding =
        FragmentTeamBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.developer1.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Companion.Developers.DEV1)
        }

        binding.developer2.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Companion.Developers.DEV2)
        }

        binding.developer3.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Companion.Developers.DEV3)
        }

        binding.developer4.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Companion.Developers.DEV4)
        }
    }
}
