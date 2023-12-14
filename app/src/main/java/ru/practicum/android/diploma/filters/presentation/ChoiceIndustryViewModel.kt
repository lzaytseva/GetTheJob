package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.domain.model.IndustryScreenState
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class ChoiceIndustryViewModel @Inject constructor(
    private val repository: GetDataRepo<Resource<List<Industry>>>
) : ViewModel() {

    private val originalList = mutableListOf<Industry>()

    private val searchDebounce: (String) -> Unit =
        debounce(SEARCH_DELAY_IN_MILLIS, viewModelScope, true) { searchText ->
            searchRequest(searchText)
        }
    private var lastSearchedText: String? = null

    private val _state = MutableLiveData<IndustryScreenState>()
    val state: LiveData<IndustryScreenState>
        get() = _state

    fun getIndustries() {
        if (originalList.isNotEmpty()) {
            _state.postValue(IndustryScreenState.Content(originalList))
            return
        }
        viewModelScope.launch {
            _state.postValue(IndustryScreenState.Loading)
            repository.get().collect {
                when (it) {
                    is Resource.Error -> {
                        _state.postValue(IndustryScreenState.Error(it.errorType!!))
                    }

                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()) {
                            _state.postValue(IndustryScreenState.Error(ErrorType.NO_CONTENT))
                        } else {
                            originalList.addAll(it.data)
                            _state.postValue(IndustryScreenState.Content(originalList))
                        }
                    }

                    null -> {}
                }
            }
        }
    }


    fun search(searchText: String) {
        if (searchText != lastSearchedText) {
            searchDebounce(searchText)
            lastSearchedText = searchText
        }
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isEmpty()) {
            getIndustries()
            return
        }
        _state.postValue(IndustryScreenState.Loading)
        val filteredList = mutableListOf<Industry>()
        originalList.forEach { industry ->
            if (industry.name.contains(searchText, true)) {
                filteredList.add(industry)
            }
        }
        if (filteredList.isEmpty()) {
            _state.postValue(IndustryScreenState.Error(ErrorType.NO_CONTENT))
        } else {
            _state.postValue(IndustryScreenState.Content(filteredList))
        }
    }

    companion object {
        private const val SEARCH_DELAY_IN_MILLIS = 2000L
    }
}
