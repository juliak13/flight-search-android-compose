package com.example.flightsearch.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.flightsearch.R

val Winky = FontFamily(
    Font(R.font.winky_sans, FontWeight.Normal),
)

val Playfair = FontFamily(
    Font(R.font.playfair_display, FontWeight.Bold)

)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Winky,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 25.sp,
        //letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Winky,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Winky,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    displayLarge = TextStyle(
        fontFamily = Playfair,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp
    )
)