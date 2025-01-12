package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.vacancydetails.domain.model.VacancyDetails
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.getSalaryDescription
import ru.practicum.android.diploma.util.scaleAnimation
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsScreenState
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsViewModel

@AndroidEntryPoint
class VacancyDetailsFragment : BindingFragment<FragmentVacancyDetailsBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModels()
    private val toolbar by lazy { (requireActivity() as RootActivity).toolbar }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyDetailsBinding =
        FragmentVacancyDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

        binding.companySection.setOnClickListener {
            viewModel.openLink()
        }

        binding.similarVacanciesBtn.setOnClickListener {
            if (viewModel.vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
                val action = VacancyDetailsFragmentDirections.actionVacancyDetailsFragmentToSimilarVacanciesFragment(
                    (viewModel.vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.id
                )
                findNavController().navigate(action)
            }

        }

        viewModel.vacancyDetailsScreenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is VacancyDetailsScreenState.Content -> showContent(screenState.vacancyDetails)
                is VacancyDetailsScreenState.Loading -> showLoading()
                is VacancyDetailsScreenState.Error -> showError(screenState.error)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar()
    }

    override fun onStop() {
        super.onStop()
        hideToolbarIcons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar.menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite)
    }

    private fun showContent(vacancyDetails: VacancyDetails) {
        with(binding) {
            loading.visibility = View.GONE
            error.visibility = View.GONE
            content.visibility = View.VISIBLE
            bindDataToViews(vacancyDetails)
        }
    }

    private fun showLoading() {
        binding.content.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE
    }

    private fun showError(error: ErrorType?) {
        binding.loading.visibility = View.GONE
        binding.content.visibility = View.GONE
        binding.error.visibility = View.VISIBLE
        if (error == ErrorType.NO_INTERNET) {
            binding.errorMessage.text = getString(R.string.error_no_internet)
            binding.errorImage.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ph_no_internet)
            )
        } else {
            binding.errorMessage.text = getString(R.string.error_server)
            binding.errorImage.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ph_server_error_vacancy)
            )
        }
    }

    private fun bindDataToViews(vacancyDetails: VacancyDetails) {
        with(binding) {
            positionName.text = vacancyDetails.name
            salary.text = getSalaryDescription(
                resources,
                vacancyDetails.salaryFrom,
                vacancyDetails.salaryTo,
                vacancyDetails.salaryCurrency
            )
            loadLogo(vacancyDetails.logoUrl240)
            companyName.text = vacancyDetails.employerName
            companyLocation.text = getCompanyLocation(vacancyDetails)
            experience.text = vacancyDetails.experience
            schedulesInfo.text = vacancyDetails.schedule ?: ""
            description.setText(
                Html.fromHtml(
                    vacancyDetails.description,
                    Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
                )
            )
            if (vacancyDetails.keySkills.isNullOrEmpty()) {
                keySkillsTitle.visibility = View.GONE
                keySkills.visibility = View.GONE
            } else {
                keySkillsTitle.visibility = View.VISIBLE
                keySkills.visibility = View.VISIBLE
                keySkills.text = getKeySkills(keySkills = vacancyDetails.keySkills)
            }
            showContactInfo(vacancyDetails)
            heartHandle(vacancyDetails.isFavoriteWrapper.isFavorite)
        }
    }

    private fun loadLogo(logoUrl: String?) {
        Glide.with(binding.content)
            .load(logoUrl)
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.vacancy_screen_logo_rounded_corners
                    )
                )
            )
            .placeholder(R.drawable.ic_vacancy_placeholder)
            .into(binding.companyLogo)
    }

    private fun getCompanyLocation(vacancyDetails: VacancyDetails): String {
        return if (vacancyDetails.address.isNullOrBlank()) {
            vacancyDetails.area
        } else {
            vacancyDetails.address
        }
    }

    private fun getKeySkills(keySkills: List<String>?): String {
        val keySkillsText = StringBuilder("")
        keySkills?.map { skill -> keySkillsText.append("  \u2022   ").append(skill).append(System.lineSeparator()) }
        return keySkillsText.toString()
    }

    private fun showContactInfo(vacancyDetails: VacancyDetails) {
        with(binding) {
            if (vacancyDetails.contactEmail.isNullOrBlank() && vacancyDetails.phones?.get(0).isNullOrBlank()) {
                contactsBlock.visibility = View.GONE
            } else {
                contacts.visibility = View.VISIBLE
                contactPersonGroup.visibility = View.VISIBLE
                contactPersonName.text = vacancyDetails.contactName
            }
            if (!vacancyDetails.contactEmail.isNullOrEmpty()) {
                contactPersonEmailGroup.visibility = View.VISIBLE
                contactPersonEmail.text = vacancyDetails.contactEmail
                contactPersonEmail.setOnClickListener { viewModel.sendEmail() }
            } else {
                contactPersonEmailGroup.visibility = View.GONE
            }
            if (!vacancyDetails.phones.isNullOrEmpty() && vacancyDetails.phones[0].isNotBlank()) {
                contactPersonPhoneGroup.visibility = View.VISIBLE
                contactPersonPhone.text = vacancyDetails.phones[0]
                contactPersonPhone.setOnClickListener {
                    viewModel.makePhoneCall()
                }
            } else {
                contactPersonPhoneGroup.visibility = View.GONE
            }
            if (!vacancyDetails.contactComment.isNullOrBlank()) {
                contactPersonCommentGroup.visibility = View.VISIBLE
                contactPersonComment.text = vacancyDetails.contactComment
            } else {
                contactPersonCommentGroup.visibility = View.GONE
            }
        }
    }

    private fun configureToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        toolbar.setTitle(getString(R.string.vacancy))
        toolbar.menu.findItem(R.id.favorite).isVisible = true
        toolbar.menu.findItem(R.id.share).isVisible = true
        toolbar.menu.findItem(R.id.filters).isVisible = false

        toolbar.menu.findItem(R.id.share).setOnMenuItemClickListener {
            viewModel.shareVacancy()
            true
        }

        toolbar.menu.findItem(R.id.favorite).setOnMenuItemClickListener { menuItem ->
            val itemView = requireActivity().findViewById<View>(menuItem.itemId)
            scaleAnimation(itemView)
            viewModel.clickInFavorites()
            true
        }
    }

    private fun heartHandle(isFavorite: Boolean) {
        if (isFavorite) {
            toolbar.menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_active)
        } else {
            toolbar.menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite)
        }
    }

    private fun hideToolbarIcons() {
        toolbar.navigationIcon = null
        toolbar.menu.findItem(R.id.favorite).isVisible = false
        toolbar.menu.findItem(R.id.share).isVisible = false
    }

}
