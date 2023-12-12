package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Content
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Loading
import ru.practicum.android.diploma.search.presentation.SearchScreenState.LoadingNextPageError
import ru.practicum.android.diploma.util.Resource
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

    // В эту переменную сохраняется текст первого запроса
    private var lastSearchedText = ""

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    private val _savedQueryState: SingleLiveEvent<String> = SingleLiveEvent()
    val savedQueryState: LiveData<String>
        get() = _savedQueryState

    private val searchRequest: (String) -> Unit = debounce(SEARCH_DELAY, viewModelScope, true) { text ->
        _screenState.postValue(Loading)

        viewModelScope.launch {
            searchRepository.search(text, currentPage)
                .singleOrNull()
                ?.processResult()
        }
    }

    fun search(text: String) {
        if (text != lastSearchedText) {
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
                searchRepository.search(lastSearchedText, currentPage)
                    .singleOrNull()
                    ?.processResult()
            }
        }
    }

    fun saveQueryState(queryState: String) {
        if (queryState != savedQueryState.value) {
            _savedQueryState.value = queryState
        }
    }

    private fun Resource<SearchResult>.processResult() {
        isNextPageLoading = false

        if (data != null) {
            vacancies.addAll(data.vacancies)
            pages = data.pages
        }

        when {
            errorType != null -> if (currentPage == 0) {
                _screenState.postValue(Error(errorType))
            } else {
                _screenState.postValue(LoadingNextPageError(errorType))
            }

            vacancies.isEmpty() -> _screenState.postValue(Error(ErrorType.NO_CONTENT))

            else -> _screenState.postValue(Content(vacancies))
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
