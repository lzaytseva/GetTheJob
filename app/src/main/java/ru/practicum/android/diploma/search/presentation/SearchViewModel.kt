package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
//    private val searchRepository: SearchRepo<VacancyInList>
) : ViewModel() {


    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    fun search(text: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            searchRepository.search(text).collect { vacancies ->
//                _screenState.postValue(SearchScreenState.Content(vacancies))
//            }
//        }
    }
}
