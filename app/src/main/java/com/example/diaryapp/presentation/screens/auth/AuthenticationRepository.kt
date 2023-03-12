package com.example.diaryapp.presentation.screens.auth

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val app: App
) {
    suspend fun signIn(
        tokenId: String
    ) = flow {
        val isLoggedIn = app.login(Credentials.jwt(tokenId))
            .loggedIn
        emit(isLoggedIn)
    }
}