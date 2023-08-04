package com.ajit.aisleassignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajit.aisleassignment.data.models.NotesResponse
import com.ajit.aisleassignment.data.repository.Repository
import com.ajit.aisleassignment.utils.UiState
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: Repository) : ViewModel() {
    private val _notesResponse = MutableLiveData<UiState<NotesResponse>>()
    val notesResponse: LiveData<UiState<NotesResponse>> = _notesResponse

    fun getNotes(authToken: String) {
        viewModelScope.launch {
            _notesResponse.value = UiState.Loading
            val response = repository.getNotes(authToken)
            _notesResponse.value = response
        }
    }
}