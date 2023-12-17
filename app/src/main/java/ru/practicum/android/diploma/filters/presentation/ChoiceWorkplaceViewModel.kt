package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import javax.inject.Inject

@HiltViewModel
class ChoiceWorkplaceViewModel @Inject constructor(
    private val getFiltersRepository: GetDataRepo<Filters>
) : ViewModel() {

    private val _screenState = MutableLiveData<ChoiceWorkplaceScreenState>()
    val screenState: LiveData<ChoiceWorkplaceScreenState> = _screenState

    init {
        getFilters()
    }

    fun getFilters() {
        viewModelScope.launch {
            getFiltersRepository.get().collect() { currentFilters ->
                if (currentFilters != null) {
                    _screenState.postValue(
                        ChoiceWorkplaceScreenState(
                            currentFilters.countryId ?: ""
                        )
                    )
                }
            }
        }
    }

}
