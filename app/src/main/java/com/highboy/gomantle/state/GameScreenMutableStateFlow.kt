package com.highboy.gomantle.state

import com.highboy.gomantle.data.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameScreenMutableStateFlow(
    val _userGuess: MutableStateFlow<String> = MutableStateFlow(""),
    val _wordHistory: MutableStateFlow<List<Word>> = MutableStateFlow(emptyList()),
    val _selectedWord: MutableStateFlow<String> = MutableStateFlow(""),
    val _isWordDescriptionVisible: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _placeHolder: MutableStateFlow<String> = MutableStateFlow("Guess the word!"),
    val _isWarningDialogShowing: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val _lastPrediction: MutableStateFlow<String> = MutableStateFlow(""),
    val _tryCount: MutableStateFlow<Int> = MutableStateFlow(0),

)
