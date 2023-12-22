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
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.presentation.state.FiltersScreenState
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FiltersViewModel @Inject constructor(
    @Named(RepositoryModule.FILTERS_TEMP_GET_REPOSITORY)
    private val getFiltersRepository: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_TEMP_SAVE_REPOSITORY)
    private val saveFiltersRepository: SaveDataRepo<Filters>,
    private val saveRefreshSearchFlagRepo: SaveDataRepo<Boolean>
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
            currentFilters = null
        }
    }

    fun updateOnlyWithSalary(isChecked: Boolean) {
        viewModelScope.launch {
            val updatedFilters = currentFilters?.copy(salaryFlag = isChecked) ?: Filters(
                salaryFlag = isChecked
            )
            saveFiltersRepository.save(updatedFilters)
            currentFilters = updatedFilters
        }
    }

    fun refreshSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            saveRefreshSearchFlagRepo.save(true)
        }
    }

    fun updateSalary(salary: String) {
        viewModelScope.launch {
            val updatedFilters = if (salary.isBlank()) {
                currentFilters?.copy(salary = null)
            } else {
                currentFilters?.copy(salary = salary) ?: Filters(salary = salary)
            }
            saveFiltersRepository.save(updatedFilters)
            currentFilters = updatedFilters
        }
    }
}
