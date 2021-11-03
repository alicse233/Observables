package com.example.observableslearning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _liveData = MutableLiveData("Hello world")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow("Hello world")
    val stateFlow: StateFlow<String> = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        _liveData.value = "Live data is triggered!"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "State flow is triggered!"
    }

    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5) {
                if (it == 4) {
                    emit("Finished processing simple flow!")
                } else {
                    emit("Items $it")
                }
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("An error occurred")
        }
    }

}