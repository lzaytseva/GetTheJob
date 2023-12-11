package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Content
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Loading
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo<SearchResult>
) : ViewModel() {
    private var pages = 0
    private var currentPage = 0
    private val vacancies = mutableListOf<VacancyInList>()
    private var isNextPageLoading = false
    private var lastSearchedText = ""

    private val _showLoadingNewPageError = SingleLiveEvent<ErrorType>()
    val showLoadingNewPageError: LiveData<ErrorType>
        get() = _showLoadingNewPageError

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    private val searchState: String get() = _screenState.value?.state ?: ""

    private val searchRequest: (String) -> Unit = debounce(SEARCH_DELAY, viewModelScope, true) { text ->
        saveQueryState(text)
        _screenState.postValue(Loading.apply { state = searchState })

        viewModelScope.launch {
            searchRepository.search(text, currentPage)
                .collect {
                    processResult(
                        searchResult = it.data,
                        error = it.errorType
                    )
                }
        }
    }

    fun search(text: String) {
        if (text != screenState.value?.state) {
            currentPage = 0
            vacancies.clear()
            lastSearchedText = text

            searchRequest(text)
        }
    }

    fun loadNextPage() {
        currentPage++

        if (currentPage != pages && !isNextPageLoading) {
            _screenState.postValue(SearchScreenState.LoadingNextPage)
            viewModelScope.launch {
                isNextPageLoading = true
                searchRepository.search(lastSearchedText, currentPage).collect {
                    processResult(
                        searchResult = it.data,
                        error = it.errorType
                    )
                }
            }
        }
    }

    fun saveQueryState(queryState: String) {
        screenState.value?.run {
            if (queryState != state) {
                state = queryState
                _screenState.postValue(this)
            }
        }
    }

    private fun processResult(searchResult: SearchResult?, error: ErrorType?) {
        isNextPageLoading = false

        if (searchResult != null) {
            vacancies.addAll(searchResult.vacancies)
            pages = searchResult.pages
        }
        when {
            error != null -> {
                //Если прилетел результат первого поиска
                if (currentPage == 0) {
                    _screenState.postValue(Error(error))
                } else {
                    //Ошибка загрузка следующей страницы
                    _showLoadingNewPageError.postValue(error)
                }
            }

            vacancies.isEmpty() -> _screenState.postValue(Error(ErrorType.NO_CONTENT))

            else -> {
                _screenState.postValue(Content(vacancies))
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
