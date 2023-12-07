package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.VacancyDetailsRepository
import javax.inject.Inject

@HiltViewModel
class VacancyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val vacancyDetailsRepository: VacancyDetailsRepository
) : ViewModel() {

    private val _vacancyDetailsScreenState = MutableLiveData<VacancyDetailsScreenState>()
    val vacancyDetailsScreenState: LiveData<VacancyDetailsScreenState> = _vacancyDetailsScreenState

    private val vacancyDetailsId = "89668568"//savedStateHandle.get<String>("vacancyDetailsId")

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

}
