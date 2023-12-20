package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Content
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Loading
import ru.practicum.android.diploma.search.presentation.SearchScreenState.LoadingNextPageError
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo<SearchResult>,
    @Named(RepositoryModule.FILTERS_GET_REPOSITORY)
    private val getFiltersRepo: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_TEMP_GET_REPOSITORY)
    private val getFiltersTempRepo: GetDataRepo<Filters>,
    @Named(RepositoryModule.FILTERS_SAVE_REPOSITORY)
    private val saveFiltersRepo: SaveDataRepo<Filters>
) : ViewModel() {

    private var pages = 0
    private var currentPage = 0
    private var found = 0
    private val vacancies = mutableListOf<Vacancy>()
    private var isNextPageLoading = false

    // В эту переменную сохраняется текст первого запроса
    private var lastSearchedText = ""

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData(SearchScreenState.NotStarted)
    val screenState: LiveData<SearchScreenState>
        get() = _screenState

    private val _savedQueryState: SingleLiveEvent<String> = SingleLiveEvent()
    val savedQueryState: LiveData<String>
        get() = _savedQueryState

    private val _filtersState: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val filtersState: LiveData<Boolean>
        get() = _filtersState

    private val searchRequest: (String?) -> Unit = debounce(SEARCH_DELAY, viewModelScope, true) { text ->
        _screenState.postValue(Loading)
        lastSearchedText = text!!

        viewModelScope.launch {
            searchRepository.search(text.trim(), currentPage)
                .singleOrNull()
                ?.processResult()
        }
    }

    fun search(text: String) {
        if (text != lastSearchedText) {
            clearSearch()
            currentPage = 0
            searchRequest(text)
        }
    }

    fun cancelSearch() {
        searchRequest(null)
        if (screenState.value !is Content) {
            lastSearchedText = ""
            currentPage = 0
        }
    }

    fun loadNextPage() {
        currentPage++

        if (currentPage != pages && !isNextPageLoading) {
            _screenState.postValue(SearchScreenState.LoadingNextPage)
            viewModelScope.launch {
                isNextPageLoading = true
                searchRepository.search(lastSearchedText, currentPage)
                    .singleOrNull()
                    ?.processResult()

            }
        }
    }

    fun clearSearch() {
        _screenState.value = SearchScreenState.NotStarted
        vacancies.clear()
        lastSearchedText = ""
        viewModelScope.launch(Dispatchers.IO) {
            val oldFilters = getFiltersRepo.get().singleOrNull()
            val newFilters = getFiltersTempRepo.get().singleOrNull()
            if (oldFilters != newFilters) {
                saveFiltersRepo.save(newFilters)
            }
        }
    }

    fun refreshSearch() {
        if (refresh_search && screenState.value is Content) {
            val tempText = lastSearchedText
            lastSearchedText = ""
            search(tempText)
        }
    }

    fun saveQueryState(queryState: String) {
        if (queryState != savedQueryState.value) {
            _savedQueryState.value = queryState
        }
    }

    fun checkFiltersIcon() {
        viewModelScope.launch {
            _filtersState.postValue(getFiltersTempRepo.get().singleOrNull()?.filtersNotNull() ?: false)
        }
    }

    private fun Resource<SearchResult>.processResult() {
        isNextPageLoading = false
        if (data != null) {
            vacancies.addAll(data.vacancies)
            pages = data.pages
            found = data.found
        }
        Log.wtf("AAA", "Result")

        when {
            errorType != null -> if (currentPage == 0) {
                _screenState.postValue(Error(errorType))
            } else {
                _screenState.postValue(LoadingNextPageError(errorType))
            }

            vacancies.isEmpty() -> _screenState.postValue(Error(ErrorType.NO_CONTENT))

            else -> _screenState.postValue(Content(vacancies, found))
        }
    }

    fun switchState() {
        if (screenState.value is LoadingNextPageError) {
            _screenState.postValue(Content(vacancies, found))
        }
    }

    private fun Filters.filtersNotNull(): Boolean =
        when {
            regionId != null -> true
            regionName != null -> true
            countryId != null -> true
            countryName != null -> true
            salary != null -> false
            salaryFlag != null && salaryFlag != false -> true
            industryId != null -> true
            industryName != null -> true
            currency != null -> true
            else -> false
        }

    companion object {
        private const val SEARCH_DELAY = 2000L
        var refresh_search = false
    }
}
