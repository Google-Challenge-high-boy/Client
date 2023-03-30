package com.highboy.gomantle.state

import kotlinx.coroutines.flow.MutableStateFlow

data class RankScreenMutableStateFlow(
    val _selectedDate: MutableStateFlow<String> = MutableStateFlow(""),
    val _myRank: MutableStateFlow<Int> = MutableStateFlow(0),
    val _allRank: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
)