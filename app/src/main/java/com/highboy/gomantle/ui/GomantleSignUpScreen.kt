package com.highboy.gomantle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                TextField(
                    value = nickname,
                    onValueChange = {
                        nickname = it
                    })
                Button(
                    onClick = { viewModel.signUp() }
                ) {
                    Text(
                        text = "Sign Up"
                    )
                }
            }
        }
    }

}