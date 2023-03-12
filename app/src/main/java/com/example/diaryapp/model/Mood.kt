package com.example.diaryapp.model

import androidx.compose.ui.graphics.Color
import com.example.diaryapp.R
import com.example.diaryapp.ui.theme.NeutralColor

enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color,
) {
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = NeutralColor
    )
}