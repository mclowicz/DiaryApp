package com.example.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.diaryapp.R
import com.example.diaryapp.data.repository.Diaries
import com.example.diaryapp.model.RequestState
import java.time.ZonedDateTime

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAllClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = onSignOutClicked,
        onDeleteAllClicked = onDeleteAllClicked,
        content = {
            val scope = rememberCoroutineScope()
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    HomeTopBar(
                        scrollBehavior = scrollBehavior,
                        onMenuClick = onMenuClick,
                        dateIsSelected = dateIsSelected,
                        onDateSelected = onDateSelected,
                        onDateReset = onDateReset,
                    )
                },
                content = {
                    padding = it
                    when (diaries) {
                        is RequestState.Success -> {
                            HomeContent(
                                paddingValues = it,
                                diaryNotes = diaries.data,
                                onClick = navigateToWriteWithArgs
                            )
                        }
                        is RequestState.Error -> {
                            EmptyPage(
                                title = "Error",
                                subtitle = "${diaries.error.message}"
                            )
                        }
                        is RequestState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        else -> {

                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                        onClick = navigateToWrite
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "New diary icon"
                        )
                    }
                }
            )
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onDeleteAllClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center,
                        content = {
                            Image(
                                modifier = Modifier.size(250.dp),
                                painter = painterResource(id = R.drawable.splash_teddy),
                                contentDescription = "Logo Image"
                            )
                        },
                    )
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = "Google Logo"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Sign Out",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected = false,
                        onClick = onSignOutClicked
                    )

                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Delete All Diaries",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        selected = false,
                        onClick = onDeleteAllClicked
                    )
                }
            )
        },
        content = content
    )
}