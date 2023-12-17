package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class ChoiceWorkplaceViewModel @Inject constructor(
    private val getFiltersRepository: GetDataRepo<Filters>,
    private val getCountryByIdRepository: GetDataByIdRepo<Resource<Country>>,
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
                    var countryName: String? = currentFilters.countryName
                    if (countryName.isNullOrBlank()) {
                        getCountryName(currentFilters.countryId)?.collect() { response ->
                            if (response is Resource.Success) {
                                countryName = response.data?.name
                                saveFiltersRepository.save(
                                    currentFilters.copy(countryName = countryName)
                                )
                            }
                        }
                    }
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

    private fun getCountryName(id: String?) = if (id.isNullOrBlank()) {
        null
    } else {
        getCountryByIdRepository.getById(id)
    }
}
