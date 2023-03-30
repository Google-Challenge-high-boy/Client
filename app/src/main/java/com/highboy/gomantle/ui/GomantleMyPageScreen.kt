package com.highboy.gomantle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleMyPageScreen(
    paddingValues: PaddingValues,
    viewModel: GomantleViewModel = viewModel()
) {
    Surface(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Box{
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .height(intrinsicSize = IntrinsicSize.Min)
            ) {
                Column() {
                    Text("NickName: ${viewModel.globalStateFlow.userName.collectAsState().value}")
                    Text("Email: ${viewModel.globalStateFlow.userEmail.collectAsState().value}")
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