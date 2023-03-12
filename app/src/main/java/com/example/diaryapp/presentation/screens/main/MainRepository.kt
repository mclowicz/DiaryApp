package com.example.diaryapp.presentation.screens.main

import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val app: App
) {

    suspend fun isUserLoggedIn() = flow {
        val user = app.currentUser
        emit(user != null && user.loggedIn)
    }
}