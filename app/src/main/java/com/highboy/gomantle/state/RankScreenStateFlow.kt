package com.highboy.gomantle.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RankScreenStateFlow(
    val selectedDate: StateFlow<String>,
    val myRank: StateFlow<Int>,
    val allRank: StateFlow<List<String>>
)