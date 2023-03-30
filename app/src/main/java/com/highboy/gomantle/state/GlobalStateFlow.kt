package com.highboy.gomantle.state

import com.highboy.gomantle.data.ViewType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class GlobalStateFlow (
    val date: StateFlow<String>,
    val userName: StateFlow<String>,
    val userId: StateFlow<Long>,
    val isSigningUp: StateFlow<Boolean>,
    val isLoading: StateFlow<Boolean>,
    val uiState: StateFlow<ViewType>,
    val userEmail: StateFlow<String>,
    val isFinished: StateFlow<Boolean>,
    val isSignedIn: StateFlow<Boolean>,
    val isSignInChecked: StateFlow<Boolean>
)