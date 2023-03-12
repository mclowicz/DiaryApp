package com.example.diaryapp.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthenticationScreenState(
    val authenticated: Boolean = false,
    val loading: Boolean = false
)

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationScreenState())
    val state: StateFlow<AuthenticationScreenState> = _state.asStateFlow()

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.signIn(tokenId)
                .onStart { _state.value = _state.value.copy(loading = true) }
                .catch {
                    onError(Exception(it))
                    _state.value = _state.value.copy(authenticated = false, loading = false)
                }
                .collect {
                    onSuccess()
//                    delay(600)
                    _state.value = _state.value.copy(authenticated = it, loading = false)
                }
        }
    }
}