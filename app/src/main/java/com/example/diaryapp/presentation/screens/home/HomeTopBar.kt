package com.example.diaryapp.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@ExperimentalMaterial3Api
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
) {
    val dateDialog = rememberSheetState()
    var pickDate by remember { mutableStateOf(LocalDate.now()) }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Hamburger menu icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            if (dateIsSelected) {
                IconButton(onClick = {
                    onDateReset()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                IconButton(onClick = {
                    dateDialog.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )

    CalendarDialog(
        state = dateDialog,
        selection = CalendarSelection.Date { localDate ->
            pickDate = localDate
            onDateSelected(
                ZonedDateTime.of(
                    pickDate,
                    LocalTime.now(),
                    ZoneId.systemDefault()
                )
            )
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true)
    )
}