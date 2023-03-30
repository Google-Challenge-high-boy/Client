package com.highboy.gomantle.ui

import android.util.Log
import android.view.RoundedCorner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerDefaults.shape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.PrefRepository
import com.highboy.gomantle.R
import com.highboy.gomantle.ui.state.GomantleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GomantleStartScreen(
    viewModel: GomantleViewModel = viewModel(),
    startSignIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Gomantle",
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF9632),
                modifier = Modifier
                    .padding(top = 40.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(50)
                        )
                        .height(
                            intrinsicSize = IntrinsicSize.Min
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .padding(start = 10.dp),
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Continue with Google",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(50),
                                )
                                .padding(10.dp)
                                .clickable {
                                    startSignIn()
                                }
                        )
                    }
                }

                Text(
                    text = "Play as guest",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(shape)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(50),
                        )
                        .padding(10.dp)
                        .clickable {
                            viewModel.updateIsSignInChecked(true)
                            PrefRepository.putString(GlobalConstants.USER_EMAIL, "guest")
                        }
                )
            }
        }
    }
}