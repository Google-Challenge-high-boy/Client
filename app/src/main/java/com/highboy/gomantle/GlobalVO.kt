package com.highboy.gomantle

import kotlinx.coroutines.flow.MutableStateFlow

class GlobalVO {
    companion object {
        var userEmail: String = ""
        var userId: Long = 0
    }
}