package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceRegionViewModel @Inject constructor(
    private val regionsByIdRepo: GetDataByIdRepo<Resource<List<Country>>>,
    @Named(RepositoryModule.REGIONS_REPOSITORY_IMPL)
    private val allRegionsRepo: GetDataRepo<Resource<List<Country>>>,
    private val getFiltersRepository: GetDataRepo<Filters>,
    private val saveFiltersRepository: SaveDataRepo<Filters>,
) : ViewModel() {

    private val _state = MutableLiveData<ChoiceRegionScreenState>()
    val state: LiveData<ChoiceRegionScreenState>
        get() = _state

    init {
        _state.postValue(ChoiceRegionScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            getFiltersRepository.get().collect { filters ->
                if (filters == null) {
                    searchAllRegions()
                } else {
                    if (filters.regionId == null) {
                        searchAllRegions()
                    } else {
                        searchRegionsById(filters.regionId)
                    }
                }
            }
        }
    }

    private suspend fun searchAllRegions() {
        allRegionsRepo.get().collect { resource ->
            handleResource(resource)
        }
    }

    private suspend fun searchRegionsById(id: String) {
        regionsByIdRepo.getById(id).collect { resource ->
            handleResource(resource)
        }
    }

    private fun handleResource(resource: Resource<List<Country>>?) {
        when (resource) {
            is Resource.Success -> {
                if (resource.data.isNullOrEmpty()) {
                    // Empty или Error???
                    _state.postValue(ChoiceRegionScreenState.Empty)
                } else {
                    _state.postValue(ChoiceRegionScreenState.Content(regions = resource.data))
                }
            }

            else -> {
                _state.postValue(ChoiceRegionScreenState.Error)
            }
        }
    }

    fun clickItem(item: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            getFiltersRepository.get().collect { filters ->
                saveFiltersRepository.save(
                    filters?.copy(regionId = item.id) ?: Filters(
                        regionId = item.id,
                        salary = null,
                        salaryFlag = null,
                        industryId = null,
                        currency = null
                    )
                )
            }
        }
    }
}
