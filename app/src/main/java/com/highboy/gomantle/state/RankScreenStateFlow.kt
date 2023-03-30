package com.highboy.gomantle.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RankScreenStateFlow(
    val selectedYear: StateFlow<Int>,
    val selectedMonth: StateFlow<Int>,
    val selectedDate: StateFlow<Int>,
    val myRank: StateFlow<Int>,
    val allRank: StateFlow<List<String>>
)