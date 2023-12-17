package ru.practicum.android.diploma.favorites.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.DeleteDataRepo
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import java.sql.SQLException
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesVacancyListRepository: FavoritesVacancyListRepository,
    private val deleteVacancyRepository: DeleteDataRepo<String>,
) : ViewModel() {

    private val _screenState: MutableLiveData<FavoritesState> = MutableLiveData()
    val screenState: LiveData<FavoritesState> get() = _screenState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favoritesVacancyListRepository.getFavoritesVacancyList().collect() { list ->
                    when {
                        list == null -> _screenState.postValue(FavoritesState.DbError)
                        list.isEmpty() -> _screenState.postValue(FavoritesState.Empty)
                        else -> _screenState.postValue(FavoritesState.Content(list))
                    }
                }
            } catch (e: SQLException) {
                _screenState.postValue(FavoritesState.DbError)
                Log.e("Tag", e.stackTraceToString())
            }
        }
    }

    fun deleteFromFavorite(vacancyId: String) {
        viewModelScope.launch {
            deleteVacancyRepository.delete(vacancyId)
        }
    }
}
