package com.highboy.gomantle.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.R
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.data.User
import kotlinx.coroutines.launch

@Composable
fun GomantleFriendsScreen(
    paddingValues: PaddingValues,
    viewModel: GomantleViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FriendInputBox()
            val listState = rememberLazyListState()
            val friendsList = viewModel.friendScreenStateFlow.friendsList.collectAsState().value
            LazyColumn(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp),
                state = listState
            ) {
                itemsIndexed(friendsList) { _, user ->
                    FriendsListItem(user = user)
                }
            }
        }
        MySnackBar()
    }
}

@Composable
fun FriendsListItem(
    user: User
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable {
            }
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.profile_img),
                    contentDescription = ""
                )
                Text(user.userName)
            }
        }
    }
}

@Composable
fun FriendInputBox(
    viewModel: GomantleViewModel = viewModel(),
) {
    val friendId = viewModel.friendScreenStateFlow.friendId.collectAsState().value
    GomantleTheme() {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .clip(CardDefaults.shape)
                            .clickable() {
                                viewModel.checkFriendId()
                                viewModel.updateFriendIdTextField("")
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = ""
                            )
                        }
                    }
                }
                BasicTextField(
                    value = friendId.toString(),
                    onValueChange = {
                        viewModel.updateFriendIdTextField(it)
                    },
                    modifier = Modifier
                        .height(intrinsicSize = IntrinsicSize.Min)
                        .padding(start = 12.dp, top = 12.dp, end = 36.dp, bottom = 12.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.checkFriendId()
                            viewModel.updateFriendIdTextField("")
                        }
                    ),
                    singleLine = true
                )
                if (viewModel.friendScreenStateFlow.friendId.collectAsState().value.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .height(intrinsicSize = IntrinsicSize.Min)
                            .padding(12.dp),
                        text = viewModel.friendScreenStateFlow.placeHolder.collectAsState().value
                    )
                }
            }
        }
    }
}

@Composable
fun MySnackBar(
    viewModel: GomantleViewModel = viewModel()
) {

    val snackBarState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    val snackbarText = viewModel.friendScreenStateFlow.snackBarText.collectAsState().value

    val buttonColor: (SnackbarData?) -> Color = { snackbarData ->
        if (snackbarData != null) {
            Color.Black
        } else {
            Color.Blue
        }
    }

    LaunchedEffect(viewModel.friendScreenStateFlow.isSnackBarShowing.collectAsState().value) {
        if (snackBarState.currentSnackbarData !=null){
            snackBarState.currentSnackbarData?.dismiss()
        }
        val result = snackBarState.showSnackbar(
            snackbarText,
            "확인",
            duration = SnackbarDuration.Short
        )
    }

}