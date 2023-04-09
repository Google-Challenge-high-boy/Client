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
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
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
        val state = rememberWebViewState(url = "https://for-google-callenge.vercel.app/?userId=1&email=txepahs@gmail.com&name=harang")
        WebView(
            modifier = Modifier
                .fillMaxSize(),
            state = state,
            onCreated = { it.settings.javaScriptEnabled = true }
        )

    }
}