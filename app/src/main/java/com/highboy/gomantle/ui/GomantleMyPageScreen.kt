package com.highboy.gomantle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GomantleMyPageScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Column() {
                
            }
        }
    }
}

@Composable
fun MyGameHistory(
    modifier: Modifier = Modifier
) {
    Box() {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                "Game History",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
        }
    }
}