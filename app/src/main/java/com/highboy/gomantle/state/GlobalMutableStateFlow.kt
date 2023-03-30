package com.highboy.gomantle.state

import com.highboy.gomantle.data.ViewType
import kotlinx.coroutines.flow.MutableStateFlow

data class GlobalMutableStateFlow(
    val _date: MutableStateFlow<String> = MutableStateFlow(""),
    val _userName: MutableStateFlow<String> = MutableStateFlow(""),
    val _userId: MutableStateFlow<Long> = MutableStateFlow(-1),
    val _isSigningUp: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val _uiState: MutableStateFlow<ViewType> = MutableStateFlow(ViewType.Game),
    val _userEmail: MutableStateFlow<String> = MutableStateFlow(""),
    val _isFinished: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isSignedIn: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isSignInChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
)
