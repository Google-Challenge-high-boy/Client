package com.highboy.gomantle.state

import com.highboy.gomantle.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FriendScreenMutableStateFlow(
    val _friendId: MutableStateFlow<String> = MutableStateFlow(""),
    val _placeHolder: MutableStateFlow<String> = MutableStateFlow("friend Id"),
    val _friendsList: MutableStateFlow<List<User>> = MutableStateFlow(listOf(
            User(912936, "cody@gmail.com", "Cody"),
            User(872607, "patricia@gmail.com", "Patricia"),
            User(884879, "justin@gmail.com", "Justin")
        )
    ),
    val _isSnackBarShowing: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _snackBarText: MutableStateFlow<String> = MutableStateFlow("")
)