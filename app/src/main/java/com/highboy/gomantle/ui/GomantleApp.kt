package com.highboy.gomantle.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme

@Composable
fun GomantleApp(
    viewModel: GomantleViewModel = viewModel(),
    startSignIn: () -> Unit,
) {
    Log.e("GomantleApp", "GomantleApp")
    Log.e("GomantleApp", viewModel.hashCode().toString())
    GomantleTheme {
        if(!viewModel.globalStateFlow.isSignInChecked.collectAsState().value) {
            GomantleStartScreen(
                startSignIn = startSignIn
            )
        } else {
            GomantleHomeScreen(
                startSignIn = startSignIn
            )
        }
    }
}