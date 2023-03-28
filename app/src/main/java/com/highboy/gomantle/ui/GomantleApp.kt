package com.highboy.gomantle.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.highboy.gomantle.GlobalVO
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme
import kotlinx.coroutines.flow.update

@Composable
fun GomantleApp(
    startSignIn: () -> Unit
) {
    val gomantleUiState = viewModel.uiState.collectAsState().value
    Log.e("gomantleUiState", gomantleUiState.toString())
    viewModel.loadSharedPreferences()
    GomantleTheme {
        if(!viewModel.isSignInChecked()) {
            GomantleStartScreen(startSignIn = startSignIn, updateIsSignInChecked = updateIsSignInChecked)
        } else {
            GomantleHomeScreen(
                gomantleUiState = gomantleUiState,
                onTabPressed = { viewType: ViewType ->
                    viewModel.updateCurrentView(viewType = viewType)
                },
                viewModel = viewModel,
                isSignedIn = isSignedIn,
                startSignIn = startSignIn
            )
        }
    }
}