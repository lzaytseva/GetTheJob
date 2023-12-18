package ru.practicum.android.diploma.filters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private var currentFilters: Filters? = null

    fun loadFilters() {
        viewModelScope.launch {
            getFiltersRepository.get().collect { filters ->
                val workplace = if (filters?.regionName == null) {
                    filters?.countryName.orEmpty()
                } else {
                    "${filters.countryName}, ${filters.regionName}"
                }
                currentFilters = filters
                _state.postValue(
                    FiltersScreenState.Settings(
                        workPlace = workplace,
                        industry = filters?.industryName.orEmpty(),
                        onlyWithSalary = filters?.salaryFlag ?: false,
                        salary = filters?.salary.orEmpty()
                    )
                )
            }
        }
    }

    fun clearFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            saveFiltersRepository.save(null)
        }
    }

    fun updateOnlyWithSalary(isChecked: Boolean) {
        viewModelScope.launch {
            val updatedFilters = currentFilters?.copy(salaryFlag = isChecked) ?: Filters(
                salaryFlag = isChecked
            )
            saveFiltersRepository.save(updatedFilters)
        }
    }

    fun updateSalary(salary: String) {
        viewModelScope.launch {
            val updatedFilters = currentFilters?.copy(salary = salary) ?: Filters(salary = salary)
            saveFiltersRepository.save(updatedFilters)
        }
    }
}
