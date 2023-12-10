package ru.practicum.android.diploma.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.search.presentation.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesVacancyListRepository: FavoritesVacancyListRepository
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    val screenState: LiveData<SearchScreenState> get() = _screenState

    fun getVacancies() {
        _screenState.postValue(SearchScreenState.Loading(""))
        var list: List<VacancyInList>
        viewModelScope.launch(Dispatchers.IO) {
            try {
                list = favoritesVacancyListRepository.getFavoritesVacancyList()
                _screenState.postValue(SearchScreenState.Content(content = list, queryState = ""))
            } catch (t: Throwable) {
                _screenState.postValue(SearchScreenState.Error(ErrorType.NO_CONTENT, ""))
            }
        }
    }
}
