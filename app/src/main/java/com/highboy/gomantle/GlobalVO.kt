package com.highboy.gomantle

import kotlinx.coroutines.flow.MutableStateFlow

class GlobalVO {
    companion object {
        val isSignInChecked = MutableStateFlow(false)
    }
}