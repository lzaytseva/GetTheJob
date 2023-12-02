package ru.practicum.android.diploma.team.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class TeamFragment : BindingFragment<FragmentTeamBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTeamBinding =
        FragmentTeamBinding.inflate(inflater, container, false)
}
