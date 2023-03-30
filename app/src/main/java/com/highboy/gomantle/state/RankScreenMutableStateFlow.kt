package com.highboy.gomantle.state

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

data class RankScreenMutableStateFlow(
    val _selectedYear: MutableStateFlow<Int> = MutableStateFlow(0),
    val _selectedMonth: MutableStateFlow<Int> = MutableStateFlow(0),
    val _selectedDayOfMonth: MutableStateFlow<Int> = MutableStateFlow(0),
    val _myRank: MutableStateFlow<Int> = MutableStateFlow(0),
    val _allRank: MutableStateFlow<List<String>> = MutableStateFlow(listOf(""))
)