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
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Content
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Error
import ru.practicum.android.diploma.search.presentation.SearchScreenState.Loading
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo<VacancyInList>
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    private val searchState: String get() = _screenState.value?.state ?: ""

    private val searchRequest: (String) -> Unit = debounce(SEARCH_DELAY, viewModelScope, true) { text ->
        saveQueryState(text)
        _screenState.postValue(Loading.apply { state = searchState })

        viewModelScope.launch {
            searchRepository.search(text)
                .singleOrNull()
                .onResult()
        }
    }

    fun search(text: String) {
        if (text != screenState.value?.state) {
            searchRequest(text)
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

    private fun List<VacancyInList>?.onResult() {
        when {
            this == null -> _screenState.postValue(
                Error(ErrorType.SERVER_ERROR).apply { state = searchState }
            )
            this.isEmpty() -> _screenState.postValue(
                Error(ErrorType.NO_CONTENT).apply { state = searchState }
            )
            else -> _screenState.postValue(
                Content(this).apply { state = searchState }
            )
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
