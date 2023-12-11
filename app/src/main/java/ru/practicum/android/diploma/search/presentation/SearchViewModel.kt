package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo<VacancyInList>
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    private val searchRequest: (String) -> Unit = debounce(SEARCH_DELAY, viewModelScope, true) { text ->
        viewModelScope.launch {
            searchRepository.search(text).collect { vacancies ->
                _screenState.postValue(SearchScreenState.Content(vacancies, text))
            }
        }
    }

    fun search(text: String) {
        saveQueryState(text)
        searchRequest(text)
    }

    fun saveQueryState(queryState: String) {
        screenState.value?.run {
            if (queryState != state) {
                state = queryState
                _screenState.postValue(this)
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
