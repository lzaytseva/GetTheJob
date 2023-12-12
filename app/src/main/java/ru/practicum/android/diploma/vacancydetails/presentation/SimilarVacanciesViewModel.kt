package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val similarVacanciesRepository: GetDataByIdRepo<Resource<List<VacancyInList>>>
) : ViewModel() {

    private val _similarVacanciesScreenState = MutableLiveData<SimilarVacanciesScreenState>()
    val similarVacanciesScreenState: LiveData<SimilarVacanciesScreenState> = _similarVacanciesScreenState

    private val vacancyId = savedStateHandle.get<String>("vacancyId")

    init {
        if (!vacancyId.isNullOrBlank()) {
            getSimilarVacancies(vacancyId)
        }
    }

    private fun getSimilarVacancies(id: String) {
        _similarVacanciesScreenState.value = SimilarVacanciesScreenState.Loading
        viewModelScope.launch {
            similarVacanciesRepository.getById(id).collect() { response ->
                if (response is Resource.Success) {
                    _similarVacanciesScreenState.postValue(response.data?.let { SimilarVacanciesScreenState.Content(it) })
                } else {
                    _similarVacanciesScreenState.postValue(SimilarVacanciesScreenState.Error)
                }
            }
        }
    }
}


