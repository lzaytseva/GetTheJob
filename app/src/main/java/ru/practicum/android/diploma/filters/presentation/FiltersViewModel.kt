package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.filters.presentation.state.FiltersScreenState
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getFiltersRepository: GetDataRepo<Filters>,
    private val saveFiltersRepository: SaveDataRepo<Filters>
) : ViewModel() {

    private val _state = MutableLiveData<FiltersScreenState>()
    val state: LiveData<FiltersScreenState>
        get() = _state

    fun loadFilters() {
        viewModelScope.launch {
            getFiltersRepository.get().collect {filters ->
                if (filters != null) {
                    _state.postValue(FiltersScreenState.Content(filters))
                }
            }
        }
    }
}
