package ru.practicum.android.diploma.filters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.filters.presentation.state.ChoiceCountryScreenState
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceCountryViewModel @Inject constructor(
    @Named(RepositoryModule.COUNTRIES_REPOSITORY_IMPL)
    private val countryRepository: GetDataRepo<Resource<List<Country>>>,
    @Named(RepositoryModule.FILTERS_TEMP_GET_REPOSITORY)
    private val getFiltersRepository: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_TEMP_SAVE_REPOSITORY)
    private val saveFiltersRepository: SaveDataRepo<Filters>
) : ViewModel() {

    private val _screenState = MutableLiveData<ChoiceCountryScreenState>()
    val screenState: LiveData<ChoiceCountryScreenState> = _screenState

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            _screenState.postValue(ChoiceCountryScreenState.Loading)
            countryRepository.get().collect { response ->
                when (response) {
                    is Resource.Error -> _screenState.postValue(ChoiceCountryScreenState.Error(response.errorType))

                    is Resource.Success -> {
                        if (!response.data.isNullOrEmpty()) {
                            _screenState.postValue(ChoiceCountryScreenState.Content(response.data))
                        } else {
                            _screenState.postValue(ChoiceCountryScreenState.Error(ErrorType.NO_CONTENT))
                        }
                    }

                    null -> {}
                }
            }
        }
    }

    fun selectCountry(country: Country) {
        viewModelScope.launch {
            getFiltersRepository.get().collect { currentFilters ->
                val updatedFilters = currentFilters?.copy(
                    countryId = country.id,
                    countryName = country.name,
                    regionId = null,
                    regionName = null
                ) ?: Filters(
                    countryId = country.id,
                    countryName = country.name
                )
                saveFiltersRepository.save(updatedFilters)
            }
        }
    }
}
