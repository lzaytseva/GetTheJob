package ru.practicum.android.diploma.vacancydetails.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsScreenState
import ru.practicum.android.diploma.vacancydetails.presentation.VacancyDetailsViewModel
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class VacancyDetailsFragment : BindingFragment<FragmentVacancyDetailsBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVacancyDetailsBinding =
        FragmentVacancyDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()

        viewModel.vacancyDetailsScreenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is VacancyDetailsScreenState.Content -> showContent(screenState.vacancyDetails)
                is VacancyDetailsScreenState.Loading -> showLoading()
                is VacancyDetailsScreenState.Error -> showError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun showError() {
        binding.loading.visibility = View.GONE
        binding.content.visibility = View.GONE
        binding.error.visibility = View.VISIBLE
    }

    private fun bindDataToViews(vacancyDetails: VacancyDetails) {
        with(binding) {
            positionName.text = vacancyDetails.name
            salary.text = getSalaryDescription(vacancyDetails)
            loadLogo(vacancyDetails.logoUrl240)
            companyName.text = vacancyDetails.employerName
            companyLocation.text = getCompanyLocation(vacancyDetails)
            experience.text = vacancyDetails.experience
            schedulesInfo.text = vacancyDetails.schedule ?: ""
            description.setText(Html.fromHtml(vacancyDetails.description, Html.FROM_HTML_MODE_COMPACT))
            if (vacancyDetails.keySkills.isNullOrEmpty()) {
                keySkillsTitle.visibility = View.GONE
                keySkills.visibility = View.GONE
            } else {
                keySkillsTitle.visibility = View.VISIBLE
                keySkills.visibility = View.VISIBLE
                keySkills.text = getKeySkills(keySkills = vacancyDetails.keySkills)
            }
            showContactInfo(vacancyDetails)
        }
    }

    private fun getSalaryDescription(vacancyDetails: VacancyDetails): String {
        return when {
            vacancyDetails.salaryFrom != null && vacancyDetails.salaryTo != null ->
                getString(
                    R.string.vacancy_salary_from_to,
                    formatSalary(vacancyDetails.salaryFrom),
                    formatSalary(vacancyDetails.salaryTo),
                    vacancyDetails.salaryCurrency
                )

            vacancyDetails.salaryFrom != null && vacancyDetails.salaryTo == null ->
                getString(
                    R.string.vacancy_salary_from,
                    formatSalary(vacancyDetails.salaryFrom),
                    vacancyDetails.salaryCurrency
                )

            vacancyDetails.salaryFrom == null && vacancyDetails.salaryTo != null ->
                getString(
                    R.string.vacancy_salary_to,
                    formatSalary(vacancyDetails.salaryTo),
                    vacancyDetails.salaryCurrency
                )

            else -> getString(R.string.vacancy_salary_not_specified)
        }
    }

    private fun formatSalary(amount: Int): String {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        return format.format(amount).replace(",", " ")
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
        keySkills?.map { skill -> keySkillsText.append("-").append(skill).append(System.lineSeparator()) }
        return keySkillsText.toString()
    }

    private fun showContactInfo(vacancyDetails: VacancyDetails) {
        with(binding) {
            if (vacancyDetails.contactEmail.isNullOrBlank() && vacancyDetails.phones?.get(0).isNullOrBlank()) {
                contacts.visibility = View.GONE
                contactPerson.visibility = View.GONE
                contactPersonName.visibility = View.GONE
                contactPersonEmailTitle.visibility = View.GONE
                contactPersonEmail.visibility = View.GONE
                contactPersonPhoneTitle.visibility = View.GONE
                contactPersonPhone.visibility = View.GONE
                contactPersonCommentTitle.visibility = View.GONE
                contactPersonComment.visibility = View.GONE
            } else {
                contacts.visibility = View.VISIBLE
                contactPerson.visibility = View.VISIBLE
                contactPersonName.visibility = View.VISIBLE
                contactPersonName.text = vacancyDetails.contactName
            }
            if (!vacancyDetails.contactEmail.isNullOrEmpty()) {
                contactPersonEmailTitle.visibility = View.VISIBLE
                contactPersonEmail.visibility = View.VISIBLE
                contactPersonEmail.text = vacancyDetails.contactEmail
            } else {
                contactPersonEmailTitle.visibility = View.GONE
                contactPersonEmail.visibility = View.GONE
            }
            if (!vacancyDetails.phones?.get(0).isNullOrBlank()) {
                contactPersonPhoneTitle.visibility = View.VISIBLE
                contactPersonPhone.visibility = View.VISIBLE
                contactPersonPhone.text = vacancyDetails.phones?.get(0)
            } else {
                contactPersonPhoneTitle.visibility = View.GONE
                contactPersonPhone.visibility = View.GONE
            }
            if (!vacancyDetails.contactComment.isNullOrBlank()) {
                contactPersonCommentTitle.visibility = View.VISIBLE
                contactPersonComment.visibility = View.VISIBLE
                contactPersonComment.text = vacancyDetails.contactComment
            } else {
                contactPersonCommentTitle.visibility = View.GONE
                contactPersonComment.visibility = View.GONE
            }
        }

    }

    private fun configureToolbar() {
        val toolbar = (requireActivity() as RootActivity).toolbar
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
    }

}


