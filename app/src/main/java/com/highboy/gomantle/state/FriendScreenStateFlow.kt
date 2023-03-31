package com.highboy.gomantle.state

import com.highboy.gomantle.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FriendScreenStateFlow(
    val friendId: StateFlow<String>,
    val placeHolder: StateFlow<String>,
    val friendsList: StateFlow<List<User>>,
    val isSnackBarShowing: StateFlow<Boolean>,
    val snackBarText: StateFlow<String>
)
