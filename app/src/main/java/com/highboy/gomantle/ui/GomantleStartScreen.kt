package com.highboy.gomantle.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GomantleStartScreen(
    startSignIn: () -> Unit,
    updateIsSignInChecked: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gomantle",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(40.dp)
        )
        Text(
            text = "Sign In",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(40.dp)
                .clickable {
                    startSignIn()
                }
        )
        Text(
            text = "Play as Guest",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(40.dp)
                .clickable {
                    updateIsSignInChecked(true)
                }
        )
    }
}