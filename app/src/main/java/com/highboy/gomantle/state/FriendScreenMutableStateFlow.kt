package com.highboy.gomantle.state

import kotlinx.coroutines.flow.MutableStateFlow

data class FriendScreenMutableStateFlow(
    val _friendsEmailList: MutableStateFlow<List<String>> = MutableStateFlow(listOf("")),
    val _friendsIdList: MutableStateFlow<List<Long>> = MutableStateFlow(listOf(0))
)