package ru.practicum.android.diploma.favorites.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesVacancyListRepository: FavoritesVacancyListRepository
) : ViewModel() {

    private val _screenState: MutableLiveData<FavoritesState> = MutableLiveData()
    val screenState: LiveData<FavoritesState> get() = _screenState

    fun getVacancies() {
        var list: List<VacancyInList>
        viewModelScope.launch(Dispatchers.IO) {
            try {
                list = favoritesVacancyListRepository.getFavoritesVacancyList()
                if (list.isEmpty()) {
                    _screenState.postValue(FavoritesState.Empty)
                } else {
                    _screenState.postValue(FavoritesState.Content(list))
                }
            } catch (e: Exception) {
                _screenState.postValue(FavoritesState.DbError)
                Log.e("Tag", e.stackTraceToString())
            }
        }
    }
}
