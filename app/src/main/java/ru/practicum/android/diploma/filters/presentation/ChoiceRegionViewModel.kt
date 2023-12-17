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
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceRegionViewModel @Inject constructor(
    private val regionsByIdRepo: GetDataByIdRepo<Resource<List<Country>>>,
    @Named(RepositoryModule.REGIONS_REPOSITORY_IMPL) private val allRegionsRepo: GetDataRepo<Resource<List<Country>>>,
    private val getFiltersRepository: GetDataRepo<Filters>,
    private val saveFiltersRepository: SaveDataRepo<Filters>,
) : ViewModel() {

    private val _state = MutableLiveData<ChoiceRegionScreenState>()
    val state: LiveData<ChoiceRegionScreenState>
        get() = _state
    private var lastSearchedText: String? = null
    private val searchDebounce: (String) -> Unit =
        debounce(SEARCH_DELAY_IN_MILLIS, viewModelScope, true) { searchText ->
            searchRequest(searchText)
        }
    private var regions: List<Country> = emptyList()

    init {
        _state.postValue(ChoiceRegionScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            getFiltersRepository.get().collect { filters ->
                if (filters == null) {
                    searchAllRegions()
                } else {
                    when {
                        filters.countryId != null -> searchRegionsById(filters.countryId)
                        filters.regionId != null -> searchRegionsById(filters.regionId)
                        else -> searchAllRegions()
                    }
                }
            }
        }
    }

    fun getRegions() {
        _state.postValue(ChoiceRegionScreenState.Content(regions))
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
                    _state.postValue(ChoiceRegionScreenState.Error)
                } else {
                    _state.postValue(ChoiceRegionScreenState.Content(regions = resource.data))
                    regions = resource.data
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
                        regionName = item.name,
                        countryId = null,
                        countryName = null,
                        salary = null,
                        salaryFlag = null,
                        industryId = null,
                        industryName = null,
                        currency = null
                    )
                )
            }
        }
    }

    fun search(text: String) {
        if (state.value is ChoiceRegionScreenState.Content || state.value is ChoiceRegionScreenState.Empty) {
            if (text != lastSearchedText) {
                searchDebounce(text)
                lastSearchedText = text
            }
        }
    }

    private fun searchRequest(text: String) {
        val contentList = regions.filter { item ->
            item.name.lowercase().contains(text.lowercase())
        }
        if (contentList.isEmpty()) {
            _state.postValue(ChoiceRegionScreenState.Empty)
        } else {
            _state.postValue(ChoiceRegionScreenState.Content(contentList))
        }
    }

    companion object {
        const val SEARCH_DELAY_IN_MILLIS = 2000L
    }
}
