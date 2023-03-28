package com.highboy.gomantle.state

import com.highboy.gomantle.data.Word
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameScreenStateFlow(
    val userGuess: StateFlow<String>,
    val wordHistory: StateFlow<List<Word>>,
    val selectedWord: StateFlow<String>,
    val isWordDescriptionVisible: StateFlow<Boolean>,
    val placeHolder: StateFlow<String>,
    val isWarningDialogShowing: StateFlow<Boolean>,
    val lastPrediction: StateFlow<String>,
    val tryCount: StateFlow<Int>
)
