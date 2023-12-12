package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val similarVacanciesRepository: GetDataByIdRepo<Resource<List<VacancyInList>>>
) : ViewModel()
