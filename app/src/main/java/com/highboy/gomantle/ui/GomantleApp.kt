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
fun GomantleApp(
    modifier: Modifier = Modifier,
    startSignIn: () -> Unit,
    isSignInChecked: Boolean,
    updateIsSignInChecked: (Boolean) -> Unit,
    isSignedIn: Boolean
) {
    val viewModel: GomantleViewModel = viewModel()
    val gomantleUiState = viewModel.uiState.collectAsState().value
    GomantleTheme {
        if(!isSignInChecked) {
            GomantleStartScreen(startSignIn = startSignIn, updateIsSignInChecked = updateIsSignInChecked)
        } else {
            GomantleHomeScreen(
                gomantleUiState = gomantleUiState,
                onTabPressed = { viewType: ViewType ->
                    viewModel.updateCurrentView(viewType = viewType)
                },
                modifier = modifier,
                viewModel = viewModel,
                isSignedIn = isSignedIn,
                startSignIn = startSignIn
            )
        }

    }

}