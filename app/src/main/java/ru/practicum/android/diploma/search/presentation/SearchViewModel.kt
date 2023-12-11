package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.VacancyInList
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepo<VacancyInList>
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    fun search(text: String) {
        saveQueryState(text)
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.search(text).collect { vacancies ->
                _screenState.postValue(SearchScreenState.Content(vacancies, text))
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
}
