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
import ru.practicum.android.diploma.filters.presentation.state.IndustryScreenState
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject

@HiltViewModel
class ChoiceIndustryViewModel @Inject constructor(
    private val repository: GetDataRepo<Resource<List<Industry>>>
) : ViewModel() {

    private val originalList = mutableListOf<Industry>()
    private var lastSelectedIndustry: Industry? = null
    private var lastSelectedIndex: Int = -1

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

    fun updateIndustrySelected(industry: Industry, position: Int, currentList: List<Industry>? = null) {
        // Не смотрите ради христа в этот код, это высер моего сознания
        val unselectedIndustry = industry.copy(selected = false)
        val selectedIndustry = industry.copy(selected = true)

        if (currentList == null) {
            if (industry.selected) {
                originalList[position] = unselectedIndustry
                lastSelectedIndustry = null
                lastSelectedIndex = -1
            } else {
                if (lastSelectedIndustry != null) {
                    originalList[lastSelectedIndex] = lastSelectedIndustry!!.copy(selected = false)
                }
                originalList[position] = selectedIndustry
                lastSelectedIndustry = selectedIndustry
                lastSelectedIndex = position
            }
            _state.postValue(IndustryScreenState.Content(originalList))
        } else {
            val newList = currentList.toMutableList()
            if (industry.selected) {
                newList[position] = industry.copy(selected = false)
                originalList[lastSelectedIndex] = lastSelectedIndustry!!.copy(selected = false)
                lastSelectedIndustry = null
                lastSelectedIndex = -1
            } else {
                if (lastSelectedIndustry != null) {
                    val lastSelectedIndexCurrent = newList.indexOf(lastSelectedIndustry)
                    if (lastSelectedIndexCurrent != -1) {
                        newList[lastSelectedIndexCurrent] = newList[lastSelectedIndexCurrent].copy(selected = false)
                    }
                    originalList[lastSelectedIndex] = lastSelectedIndustry!!.copy(selected = false)
                }
                lastSelectedIndex = originalList.indexOf(industry)
                lastSelectedIndustry = selectedIndustry
                newList[position] = selectedIndustry
                originalList[lastSelectedIndex] = lastSelectedIndustry!!
            }
            _state.postValue(IndustryScreenState.Content(newList))
        }
    }

    companion object {
        private const val SEARCH_DELAY_IN_MILLIS = 2000L
    }
}
