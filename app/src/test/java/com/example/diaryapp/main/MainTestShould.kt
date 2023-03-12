package com.example.diaryapp.main

import com.example.diaryapp.InstantTaskExecutorExtension
import com.example.diaryapp.navigation.Screen
import com.example.diaryapp.presentation.screens.main.MainRepository
import com.example.diaryapp.presentation.screens.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class MainTestShould {

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: MainRepository

    @Test
    fun navigateToAuthRouteWhenNotLoggedIn() = runTest {
        repository = mock<MainRepository>().stub {
            onBlocking { isUserLoggedIn() }.thenReturn(flow { emit(false) })
        }
        viewModel = MainViewModel(repository)
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest { }
        }

        viewModel.getStartDestination()
        runCurrent()

        assertEquals(
            Screen.Authentication.route,
            viewModel.state.value.startDestination
        )
        job.cancel()
    }

    @Test
    fun navigateToHomeScreenWhenLoggedIn() = runTest {
        repository = mock<MainRepository>().stub {
            onBlocking { isUserLoggedIn() }.thenReturn(flow { emit(true) })
        }
        viewModel = MainViewModel(repository)
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest { }
        }

        viewModel.getStartDestination()
        runCurrent()

        assertEquals(
            Screen.Home.route,
            viewModel.state.value.startDestination
        )
        job.cancel()
    }
}