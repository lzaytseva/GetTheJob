package ru.practicum.android.diploma.filters.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.filters.presentation.state.ChoiceRegionScreenState
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceRegionViewModel @Inject constructor(
    private val regionsByIdRepo: GetDataByIdRepo<Resource<List<Country>>>,
    @Named(RepositoryModule.REGIONS_REPOSITORY_IMPL)
    private val allRegionsRepo: GetDataRepo<Resource<List<Country>>>,
    @Named(RepositoryModule.FILTERS_TEMP_GET_REPOSITORY)
    private val getFiltersRepository: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_TEMP_SAVE_REPOSITORY)
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
    private var regions: MutableList<Country> = mutableListOf()
    private var countries: MutableList<Country> = mutableListOf()

    init {
        _state.postValue(ChoiceRegionScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            getFiltersRepository.get().collect { filters ->
                when {
                    filters == null -> searchRegions(null)
                    filters.countryId != null -> searchRegions(filters.countryId)
                    filters.regionId != null -> searchRegions(filters.regionId)
                    else -> searchRegions(null)
                }
            }
        }
    }

    private suspend fun searchRegions(id: String?) {
        if (id == null) {
            allRegionsRepo.get().collect { resource ->
                handleResource(resource)
            }
        } else {
            regionsByIdRepo.getById(id).collect { resource ->
                handleResource(resource)
            }
        }
    }

    fun getRegions() {
        _state.postValue(ChoiceRegionScreenState.Content(regions))
    }

    private fun handleResource(resource: Resource<List<Country>>?) {
        when {
            resource is Resource.Success && resource.data.isNullOrEmpty() -> {
                _state.postValue(ChoiceRegionScreenState.Error)
            }

            resource is Resource.Success -> {
                countries.clear()
                regions.clear()
                resource.data!!.forEach {
                    if (it.parentId == null) {
                        countries.add(it)
                    } else {
                        regions.add(it)
                    }
                }
                _state.postValue(ChoiceRegionScreenState.Content(regions = regions))
            }

            else -> {
                _state.postValue(ChoiceRegionScreenState.Error)
            }
        }
    }

    fun clickItem(item: Country) {
        var firstLevelItem = regions.find {
            it.id == item.parentId
        }
        if (firstLevelItem == null) {
            firstLevelItem = regions.find {
                it.id == item.id
            }
        }
        val country = countries.find {
            it.id == firstLevelItem?.parentId
        }
        viewModelScope.launch(Dispatchers.IO) {
            getFiltersRepository.get().collect { filters ->
                saveFiltersRepository.save(
                    filters?.copy(
                        regionId = item.id,
                        regionName = item.name,
                        countryId = country?.id,
                        countryName = country?.name
                    ) ?: Filters(
                        regionId = item.id,
                        regionName = item.name,
                        countryId = country?.id,
                        countryName = country?.name
                    )
                )
            }
        }
    }

    fun search(text: String) {
        if ((state.value is ChoiceRegionScreenState.Content || state.value is ChoiceRegionScreenState.Empty) &&
            text != lastSearchedText
        ) {
            searchDebounce(text)
            lastSearchedText = text
        }
    }

    fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
        getRegions()
    }

    private fun searchRequest(text: String) {
        val contentList = regions.filter { item ->
            item.name.lowercase().contains(text.lowercase().trim())
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
