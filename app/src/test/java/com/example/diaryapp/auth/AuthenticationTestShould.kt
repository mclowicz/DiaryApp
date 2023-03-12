package com.example.diaryapp.auth

import com.example.diaryapp.InstantTaskExecutorExtension
import com.example.diaryapp.presentation.screens.auth.AuthenticationRepository
import com.example.diaryapp.presentation.screens.auth.AuthenticationScreenState
import com.example.diaryapp.presentation.screens.auth.AuthenticationViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class AuthenticationTestShould {

    private lateinit var viewModel: AuthenticationViewModel
    private var repository: AuthenticationRepository = mock()

    @Test
    fun authorizeUser() = runTest {
        val viewModel = mockSuccessfulCase()

        val job = launch(NonCancellable) {
            viewModel.state.collect()
        }

        viewModel.signInWithMongoAtlas("tokenId", {}, {})
        runCurrent()

        assertEquals(
            AuthenticationScreenState(
                authenticated = true,
                loading = false
            ),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun unAuthorizeUser() = runTest {
        val viewModel = mockUnSuccessfulCase()

        val job = launch(NonCancellable) {
            viewModel.state.collect()
        }

        viewModel.signInWithMongoAtlas("tokenId", {}, {})
        runCurrent()

        assertEquals(
            AuthenticationScreenState(
                authenticated = false,
                loading = false
            ),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun unAuthorizeOnError() = runTest {
        val viewModel = mockErrorCase()

        val job = launch(NonCancellable) {
            viewModel.state.collect()
        }

        viewModel.signInWithMongoAtlas("tokenId", {}, {})
        runCurrent()

        assertEquals(
            AuthenticationScreenState(
                authenticated = false,
                loading = false
            ),
            viewModel.state.value
        )
        job.cancel()
    }

    private suspend fun mockSuccessfulCase(): AuthenticationViewModel {
        whenever(repository.signIn("tokenId")).thenReturn(
            flow {
                emit(true)
            }
        )
        return AuthenticationViewModel(repository)
    }

    private suspend fun mockUnSuccessfulCase(): AuthenticationViewModel {
        whenever(repository.signIn("tokenId")).thenReturn(
            flow {
                emit(false)
            }
        )
        return AuthenticationViewModel(repository)
    }

    private suspend fun mockErrorCase(): AuthenticationViewModel {
        given(repository.signIn("tokenId")).willAnswer { throw Throwable() }
        return AuthenticationViewModel(repository)
    }
}