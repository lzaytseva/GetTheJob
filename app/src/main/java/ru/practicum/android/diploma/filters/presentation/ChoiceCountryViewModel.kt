package ru.practicum.android.diploma.filters.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChoiceCountryViewModel @Inject constructor(
    @Named(RepositoryModule.COUNTRIES_REPOSITORY_IMPL)
    private val countryRepository: GetDataRepo<Resource<List<Country>>>,
    @ApplicationContext
    private val context: Context
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
                            val filteredCountries = filterDefaultCountries(response.data)
                            _screenState.postValue(ChoiceCountryScreenState.Content(filteredCountries))
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

    fun showAllCountries() {
        _screenState.value = ChoiceCountryScreenState.Content(allCountries, true)
    }

    private fun filterDefaultCountries(countries: List<Country>): List<Country> {
        val filteredCountries = mutableListOf<Country>()
        countries.forEach { country ->
            if (isCountryDefault(country)) {
                filteredCountries.add(country)
            }
        }
        filteredCountries.add(Country(id = "", name = context.resources.getString(R.string.other_regions)))
        return filteredCountries
    }

    private fun isCountryDefault(country: Country): Boolean =
        defaultCountries.contains(country.name)

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
            _screenState.value = ChoiceCountryScreenState.Content(filtered, true)
        }
    }

    companion object {
        private const val FILTER_DELAY_IN_MILLIS = 2000L
        private val defaultCountries = setOf<String>(
            "Россия",
            "Украина",
            "Беларусь",
            "Казахстан",
            "Азербайджан",
            "Грузия",
            "Кыргызстан",
            "Узбекистан",
        )
    }

}
