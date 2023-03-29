package com.highboy.gomantle.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.PrefRepository
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleStartScreen(
    viewModel: GomantleViewModel = viewModel(),
    startSignIn: () -> Unit
) {
    Log.e("GomantleStartScreen", viewModel.hashCode().toString())
    Log.e("GomantleStartScreen", "GomantleStartScreen")
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
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(40.dp)
                .clickable {
                    startSignIn()
                }
        )
        Text(
            text = "Play as Guest",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(40.dp)
                .clickable {
                    viewModel.updateIsSignInChecked(true)
                    PrefRepository.putString(GlobalConstants.USER_EMAIL, "guest")
                }
        )
    }
}