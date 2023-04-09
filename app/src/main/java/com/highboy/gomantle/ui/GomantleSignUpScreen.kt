package com.highboy.gomantle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highboy.gomantle.ui.state.GomantleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GomantleSignUpScreen(
    viewModel: GomantleViewModel = viewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var nickname by remember { mutableStateOf("") }
                Text(
                    text = "Enter your nickname",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                TextField(
                    value = nickname,
                    onValueChange = {
                        nickname = it
                    })
                Button(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    onClick = { viewModel.signUp(nickname) }
                ) {
                    Text(
                        text = "Sign Up"
                    )
                }
            }
        }
    }

}