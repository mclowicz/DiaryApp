package com.example.diaryapp.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryapp.data.repository.MongoDB
import com.example.diaryapp.model.GalleryImage
import com.example.diaryapp.model.Mood
import com.example.diaryapp.presentation.components.DisplayAlertDialog
import com.example.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.example.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.example.diaryapp.presentation.screens.home.HomeScreen
import com.example.diaryapp.presentation.screens.home.HomeViewModel
import com.example.diaryapp.presentation.screens.write.WriteScreen
import com.example.diaryapp.presentation.screens.write.WriteViewModel
import com.example.diaryapp.util.Constants.APP_ID
import com.example.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.example.diaryapp.model.RequestState
import com.example.diaryapp.model.rememberGalleryState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalMaterial3Api
@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit, block = {
            onDataLoaded()
        })

        AuthenticationScreen(
            authenticated = state.authenticated,
            loadingState = state.loading,
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            onButtonClick = {
                oneTapState.open()
            },
            onSuccessfulFirebaseSignIn = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated!")
                    },
                    onError = { exception ->
                        messageBarState.addError(exception)
                    }
                )
            },
            onFailedFirebaseSignIn = {
                messageBarState.addError(it)
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
            },
            navigateToHome = navigateToHome
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val diaries by viewModel.diaries
        val context = LocalContext.current
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = diaries, block = {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        })

        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onMenuClick = { scope.launch { drawerState.open() } },
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = { viewModel.getDiaries(it) },
            onDateReset = { viewModel.getDiaries() },
            onSignOutClicked = { signOutDialogOpened = true },
            onDeleteAllClicked = {
                Toast.makeText(
                    context,
                    "All diaries deleted.",
                    Toast.LENGTH_SHORT
                ).show()
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
        )

        LaunchedEffect(Unit) {
            MongoDB.configureTheRealm()
        }

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            }
        )

        DisplayAlertDialog(
            title = "Delete All Diaries",
            message = "Are you sure you want to permanently delete all your diaries?",
            dialogOpened = deleteAllDialogOpened,
            onCloseDialog = { deleteAllDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    viewModel.deleteAllDiaries(
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "All diaries deleted.",
                                Toast.LENGTH_SHORT
                            ).show()
                            scope.launch { drawerState.close() }
                        },
                        onError = {
                            Toast.makeText(
                                context,
                                if (it.message == "No internet connection.")
                                    "We ned an internet connection for this operation."
                                else
                                    it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            scope.launch { drawerState.close() }
                        },
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel: WriteViewModel = hiltViewModel()
        val context: Context = LocalContext.current
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState()
        val galleryState = viewModel.galleryState
        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }

        LaunchedEffect(key1 = uiState, block = {
            Log.d("SelectedDiary", "${uiState.selectedDiaryId}")
        })

        WriteScreen(
            uiState = uiState,
            moodName = { Mood.values()[pageNumber].name },
            pagerState = pagerState,
            galleryState = galleryState,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        onBackPressed()
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            },
            onUpdatedDateTime = { viewModel.updateDateTime(zonedDateTime = it) },
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.values()[pageNumber].name },
                    onSuccess = onBackPressed,
                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                )
            },
            onImageSelect = { image ->
                val type = context.contentResolver.getType(image)?.split("/")?.last() ?: "jpg"
                Log.d("WriteViewModel", image.toString())
                viewModel.addImage(image = image, imageType = type)
            },
            onImageDeleteClicked = { galleryState.removeImage(it) }
        )
    }
}