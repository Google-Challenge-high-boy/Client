package com.highboy.gomantle.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme

@Composable
fun GomantleApp(modifier: Modifier = Modifier) {
    val viewModel: GomantleViewModel = viewModel()
    val gomantleUiState = viewModel.uiState.collectAsState().value
    GomantleTheme() {
        GomantleHomeScreen(
            gomantleUiState = gomantleUiState,
            onTabPressed = { viewType: ViewType ->
                viewModel.updateCurrentView(viewType = viewType)
            },
            modifier = modifier,
            viewModel = viewModel
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GomantleAppPreview() {
    GomantleTheme() {
        GomantleApp()
    }
}