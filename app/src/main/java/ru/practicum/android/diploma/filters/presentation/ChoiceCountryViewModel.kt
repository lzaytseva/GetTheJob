package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class ChoiceCountryViewModel @Inject constructor(
    private val countryRepository: GetDataRepo<Resource<List<Country>>>
) : ViewModel() {

    private val _screenState = MutableLiveData<ChoiceCountryScreenState>()
    val screenState: LiveData<ChoiceCountryScreenState> = _screenState

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            _screenState.postValue(ChoiceCountryScreenState.Loading)
            countryRepository.get().collect() { response ->
                when (response) {
                    is Resource.Error -> _screenState.postValue(ChoiceCountryScreenState.Error(response.errorType))

                    is Resource.Success -> {
                        if (!response.data.isNullOrEmpty()) {
                            _screenState.postValue(ChoiceCountryScreenState.Content(response.data))
                        } else {
                            _screenState.postValue(ChoiceCountryScreenState.Error(ErrorType.NO_CONTENT))
                        }
                    }

                    null -> {}
                }
            }
        }
    }

}
