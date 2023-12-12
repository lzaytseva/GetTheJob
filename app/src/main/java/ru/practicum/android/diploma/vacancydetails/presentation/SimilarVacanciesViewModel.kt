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
import ru.practicum.android.diploma.vacancydetails.data.SimilarVacanciesRepoImpl
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val similarVacanciesRepository: GetDataByIdRepo<Resource<List<VacancyInList>>>
) : ViewModel()
