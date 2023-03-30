package com.highboy.gomantle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GomantleLoadingScreen() {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFFFFFFF)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gomantle",
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF9632)
            )
            Text(
                text = "Guess the word!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFF9632),
                modifier = Modifier
                    .padding(bottom = 40.dp)
            )
        }
    }
}