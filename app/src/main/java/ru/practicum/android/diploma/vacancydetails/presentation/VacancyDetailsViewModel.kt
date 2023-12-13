package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.models.EmailData
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.DeleteVacancyRepository
import ru.practicum.android.diploma.vacancydetails.domain.api.SaveVacancyRepository
import javax.inject.Inject

@HiltViewModel
class VacancyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getByIdVacancyDetailsRepoImpl: GetDataByIdRepo<Resource<VacancyDetails>>,
    private val externalNavigator: ExternalNavigator,
    private val saveVacancyRepository: SaveVacancyRepository,
    private val deleteVacancyRepository: DeleteVacancyRepository,
) : ViewModel() {

    private val _vacancyDetailsScreenState = MutableLiveData<VacancyDetailsScreenState>()
    val vacancyDetailsScreenState: LiveData<VacancyDetailsScreenState> = _vacancyDetailsScreenState

    private val vacancyId = savedStateHandle.get<String>("vacancyId")

    init {
        if (!vacancyId.isNullOrBlank()) {
            getVacancyDetailsById(vacancyId)
        }
    }

    private fun getVacancyDetailsById(id: String) {
        _vacancyDetailsScreenState.value = VacancyDetailsScreenState.Loading
        viewModelScope.launch {
            getByIdVacancyDetailsRepoImpl.getById(id).collect() { response ->
                if (response is Resource.Success) {
                    _vacancyDetailsScreenState.postValue(response.data?.let { VacancyDetailsScreenState.Content(it) })
                } else {
                    _vacancyDetailsScreenState.postValue(VacancyDetailsScreenState.Error(response?.errorType))
                }
            }
        }
    }

    fun shareVacancy() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            val screenState = vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content
            externalNavigator.share(screenState.vacancyDetails.url)
        }
    }

    fun makePhoneCall() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            val screenState = vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content
            screenState.vacancyDetails.phones?.let {
                externalNavigator.makePhoneCall(
                    it[0]
                )
            }
        }
    }

    fun sendEmail() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            val screenState = vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content
            externalNavigator.sendEmail(
                EmailData(
                    emailAddress = screenState.vacancyDetails.contactEmail
                        ?: "",
                    subject = screenState.vacancyDetails.name,
                    text = ""
                )
            )
        }
    }

    fun openLink() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            val screenState = vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content
            screenState.vacancyDetails.employerUrl?.let { url ->
                externalNavigator.openUrlLink(url)
            }
        }
    }

    fun clickInFavorites() {
        if (vacancyDetailsScreenState.value is VacancyDetailsScreenState.Content) {
            if (
                (vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content)
                    .vacancyDetails
                    .isFavoriteWrapper
                    .isFavorite
            ) {
                _vacancyDetailsScreenState.postValue(
                    (_vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).apply {
                        vacancyDetails.isFavoriteWrapper.isFavorite = false
                    }
                )
                viewModelScope.launch(Dispatchers.IO) {
                    deleteVacancyRepository.deleteVacancy(vacancyId!!)
                }
            } else {
                _vacancyDetailsScreenState.postValue(
                    (_vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).apply {
                        vacancyDetails.isFavoriteWrapper.isFavorite = true
                    }
                )
                viewModelScope.launch(Dispatchers.IO) {
                    saveVacancyRepository.saveVacancy(
                        (_vacancyDetailsScreenState.value as VacancyDetailsScreenState.Content).vacancyDetails
                    )
                }
            }
        }
    }

}
