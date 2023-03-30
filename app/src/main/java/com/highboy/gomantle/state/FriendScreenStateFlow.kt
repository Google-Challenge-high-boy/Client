package com.highboy.gomantle.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FriendScreenStateFlow(
    val friendsEmailList: StateFlow<List<String>>,
    val friendsIdList: StateFlow<List<Long>>
)
