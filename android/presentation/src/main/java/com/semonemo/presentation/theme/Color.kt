package com.semonemo.presentation.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Blue1 = Color(0xFF8398A2)
val Blue2 = Color(0xFF526788)
val Blue3 = Color(0xFF267DBC)
val GunMetal = Color(0xFF252A34)
val White = Color(0xFFFFFFFF)
val Gray01 = Color(0xFF555555)
val Gray02 = Color(0xFF949494)
val Gray03 = Color(0xFFD9D9D9)
val WhiteGray = Color(0xFFEEEEEE)
val CiTest = Color(0xFF000000)
val Main01 =
    Brush.linearGradient(
        colors =
            listOf(
                White,
                Color(0xFFEBF0FF),
            ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f),
    )
val Main02 =
    Brush.horizontalGradient(
        colors =
            listOf(
                Color(0xFF535353),
                Color(0xFF273B71),
            ),
        startX = 0f,
        endX = Float.POSITIVE_INFINITY,
    )
