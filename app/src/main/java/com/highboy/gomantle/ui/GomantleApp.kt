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
import com.highboy.gomantle.state.*
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme
import kotlinx.coroutines.flow.update

@Composable
fun GomantleApp(
    globalStateFlow: GlobalStateFlow,
    gameScreenStateFlow: GameScreenStateFlow,
    friendScreenStateFlow: FriendScreenStateFlow,
    rankScreenStateFlow: RankScreenStateFlow,
    myPageScreenStateFlow: MyPageScreenStateFlow,
    startSignIn: () -> Unit,
    updateIsSignInChecked: (Boolean) -> Unit,
    updateCurrentView: (ViewType) -> Unit
) {
    GomantleTheme {
        if(!globalStateFlow.isSignInChecked.collectAsState().value) {
            GomantleStartScreen(startSignIn = startSignIn, updateIsSignInChecked = updateIsSignInChecked)
        } else {
            GomantleHomeScreen(
                globalStateFlow = globalStateFlow,
                gameScreenStateFlow = gameScreenStateFlow,
                friendScreenStateFlow = friendScreenStateFlow,
                rankScreenStateFlow = rankScreenStateFlow,
                myPageScreenStateFlow = myPageScreenStateFlow,
                updateCurrentView = updateCurrentView,
            )
        }
    }
}