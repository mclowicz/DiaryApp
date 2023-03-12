package com.example.diaryapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenState(
    val startDestination: String = String()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        getStartDestination()
    }

    fun getStartDestination() {
        viewModelScope.launch {
            repository.isUserLoggedIn()
                .collect { isLoggedIn ->
                    val route = if (isLoggedIn) {
                        Screen.Home.route
                    } else {
                        Screen.Authentication.route
                    }
                    val updatedState = _state.value.copy(startDestination = route)
                    _state.value = updatedState
                }
        }
    }
}