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
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    MyInfo(modifier = Modifier.weight(1f))
                    MyInfo(modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    MyInfo(modifier = Modifier.weight(1f))
                    MyInfo(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MyInfo(
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                "Game History",
                modifier = Modifier
                    .padding(20.dp)
            )
        }
    }
}