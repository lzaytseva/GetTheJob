package ru.practicum.android.diploma.filters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.presentation.state.ChoiceWorkplaceScreenState
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceWorkplaceViewModel @Inject constructor(
    @Named(RepositoryModule.FILTERS_TEMP_GET_REPOSITORY)
    private val getFiltersRepository: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_TEMP_SAVE_REPOSITORY)
    private val saveFiltersRepository: SaveDataRepo<Filters>,
) : ViewModel() {

    private val _screenState = MutableLiveData<ChoiceWorkplaceScreenState>()
    val screenState: LiveData<ChoiceWorkplaceScreenState> = _screenState

    init {
        getFilters()
    }

    fun getFilters() {
        viewModelScope.launch {
            getFiltersRepository.get().collect() { currentFilters ->
                if (currentFilters != null) {
                    val countryName: String? = currentFilters.countryName
                    val isBtnSelectVisible = countryName != null || currentFilters.regionName != null
                    _screenState.postValue(
                        ChoiceWorkplaceScreenState(
                            country = countryName,
                            region = currentFilters.regionName,
                            isBtnSelectVisible = isBtnSelectVisible
                        )
                    )
                }
            }
        }
    }

    fun deleteCountryRegion() {
        viewModelScope.launch {
            getFiltersRepository.get().collect { currentFilters ->
                saveFiltersRepository.save(
                    currentFilters?.copy(
                        regionId = null,
                        regionName = null,
                        countryId = null,
                        countryName = null
                    )
                )
            }
        }
    }

    fun deleteRegion() {
        viewModelScope.launch {
            getFiltersRepository.get().collect { currentFilters ->
                saveFiltersRepository.save(
                    currentFilters?.copy(
                        regionId = null,
                        regionName = null
                    )
                )
            }
        }
    }
}
