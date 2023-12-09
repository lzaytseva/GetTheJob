package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator
import ru.practicum.android.diploma.core.domain.models.EmailData
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.VacancyDetailsRepository
import javax.inject.Inject

@HiltViewModel
class VacancyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val vacancyDetailsRepository: VacancyDetailsRepository,
    private val externalNavigator: ExternalNavigator
) : ViewModel() {

    private val _vacancyDetailsScreenState = MutableLiveData<VacancyDetailsScreenState>()
    val vacancyDetailsScreenState: LiveData<VacancyDetailsScreenState> = _vacancyDetailsScreenState

    private val vacancyDetailsId = savedStateHandle.get<String>("vacancyId")

    init {
        if (!vacancyDetailsId.isNullOrBlank()) {
            getVacancyDetailsById(vacancyDetailsId)
        }
    }

    private fun getVacancyDetailsById(id: String) {
        _vacancyDetailsScreenState.value = VacancyDetailsScreenState.Loading
        viewModelScope.launch {
            vacancyDetailsRepository.getVacancyDetailsById(id).collect() { response ->
                if (response is Resource.Success) {
                    _vacancyDetailsScreenState.postValue(response.data?.let { VacancyDetailsScreenState.Content(it) })
                } else {
                    _vacancyDetailsScreenState.postValue(VacancyDetailsScreenState.Error)
                }
            }
        }
    }

    fun shareVacancy() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            externalNavigator.share(
                (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.url
            )
        }
    }

    fun makePhoneCall() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.phones?.let {
                externalNavigator.makePhoneCall(
                    it[0]
                )
            }
        }
    }

    fun sendEmail() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            externalNavigator.sendEmail(
                EmailData(
                    emailAddress = (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.contactEmail
                        ?: "",
                    subject = (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.name,
                    text = ""
                )

            )
        }
    }

    fun openLink() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails.employerUrl?.let { url ->
                externalNavigator.openUrlLink(
                    url
                )
            }
        }
    }

}
