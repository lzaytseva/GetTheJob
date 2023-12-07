package ru.practicum.android.diploma.vacancydetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class VacancyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _vacancyDetailsScreenState = MutableLiveData<VacancyDetailsScreenState>()
    val vacancyDetailsScreenState: LiveData<VacancyDetailsScreenState> = _vacancyDetailsScreenState



}
