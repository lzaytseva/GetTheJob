package ru.practicum.android.diploma.team.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.team.presentation.TeamViewModel
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.FeedbackUtils
import ru.practicum.android.diploma.util.ToolbarUtils.hideMenu

@AndroidEntryPoint
class TeamFragment : BindingFragment<FragmentTeamBinding>() {

    private val viewModel: TeamViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTeamBinding =
        FragmentTeamBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()

        viewModel.showError.observe(viewLifecycleOwner) {
            FeedbackUtils.showSnackbar(root = requireView(), text = it)
        }

        binding.developer1Name.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Developers.DEV1)
            openEnvelopeAnimation(binding.developer1Mail)
        }

        binding.developer2Name.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Developers.DEV2)
        }

        binding.developer3Name.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Developers.DEV3)
        }

        binding.developer4Name.setOnClickListener {
            viewModel.contactWithDeveloper(TeamViewModel.Developers.DEV4)
        }
    }

    private fun openEnvelopeAnimation(imageView: ImageView) {
        imageView.setImageDrawable(
            AppCompatResources.getDrawable(requireContext(), R.drawable.avd_open_mail)
        )
        (imageView.drawable as AnimatedVectorDrawable).start()
    }

    private fun closeEnvelopeAnimation(imageView: ImageView) {
        imageView.setImageDrawable(
            AppCompatResources.getDrawable(requireContext(), R.drawable.avd_close_mail)
        )
        (imageView.drawable as AnimatedVectorDrawable).start()
    }

    private fun configureToolbar() {
        val toolbar = (requireActivity() as RootActivity).toolbar
        toolbar.title = getString(R.string.team)
        toolbar.hideMenu()
        toolbar.navigationIcon = null
    }
}
