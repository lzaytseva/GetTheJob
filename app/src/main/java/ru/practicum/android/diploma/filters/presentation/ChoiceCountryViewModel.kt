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
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class ChoiceCountryViewModel @Inject constructor(
    private val countryRepository: GetDataRepo<Resource<List<Country>>>
) : ViewModel() {

    private val _screenState = MutableLiveData<ChoiceCountryScreenState>()
    val screenState: LiveData<ChoiceCountryScreenState> = _screenState

    private val allCountries = mutableListOf<Country>()
    private val filterDebounce: (String) -> Unit =
        debounce(FILTER_DELAY_IN_MILLIS, viewModelScope, true) { query ->
            filter(query)
        }
    private var lastSearchedText: String? = null

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            _screenState.postValue(ChoiceCountryScreenState.Loading)
            countryRepository.get().collect() { response ->
                when (response) {
                    is Resource.Error -> _screenState.postValue(ChoiceCountryScreenState.Error(response.errorType))

                    is Resource.Success -> {
                        if (!response.data.isNullOrEmpty()) {
                            allCountries.addAll(response.data)
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

    fun filterCountries(query: String) {
        if (lastSearchedText?.trim() != query.trim()) {
            filterDebounce(query)
            lastSearchedText = query
        }
    }

    private fun filter(query: String) {
        val filtered = mutableListOf<Country>()
        _screenState.value = ChoiceCountryScreenState.Loading
        if (allCountries.isEmpty()) {
            getCountries()
        }
        allCountries.forEach { country ->
            if (country.name.contains(query, true)) {
                filtered.add(country)
            }
        }
        if (filtered.isEmpty()) {
            _screenState.value = ChoiceCountryScreenState.Error(ErrorType.NO_CONTENT)
        } else {
            _screenState.value = ChoiceCountryScreenState.Content(filtered)
        }
    }

    companion object {
        private const val FILTER_DELAY_IN_MILLIS = 2000L
    }

}
