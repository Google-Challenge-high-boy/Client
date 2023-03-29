package com.highboy.gomantle.state

import com.highboy.gomantle.data.ViewType
import kotlinx.coroutines.flow.MutableStateFlow

data class GlobalMutableStateFlow(
    val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true),
    val _uiState: MutableStateFlow<ViewType> = MutableStateFlow(ViewType.Game),
    val _userEmail: MutableStateFlow<String> = MutableStateFlow(""),
    val _isFinished: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isSignedIn: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _isSignInChecked: MutableStateFlow<Boolean> = MutableStateFlow(false)
)
