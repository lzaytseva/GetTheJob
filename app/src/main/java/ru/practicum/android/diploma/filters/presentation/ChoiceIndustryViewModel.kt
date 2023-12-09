package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.domain.model.IndustryScreenState
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

@HiltViewModel
class ChoiceIndustryViewModel @Inject constructor(
    private val repository: GetDataRepo<Resource<List<Industry>>>
) : ViewModel() {

    private val fullList = mutableListOf<Industry>()
    private val _state = MutableLiveData<IndustryScreenState>()
    val state: LiveData<IndustryScreenState>
        get() = _state

    init {
        getIndustries()
    }

    private fun getIndustries() {
        viewModelScope.launch {
            repository.get().collect {
                when (it) {
                    is Resource.Error -> {
                        _state.postValue(IndustryScreenState.Error(it.message.orEmpty()))
                    }

                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()) {
                            _state.postValue(IndustryScreenState.Empty)
                        } else {
                            fullList.addAll(it.data)
                            _state.postValue(IndustryScreenState.Content(fullList))
                        }
                    }

                    null -> {}
                }
            }
        }
    }
}
