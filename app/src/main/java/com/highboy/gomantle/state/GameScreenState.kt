package com.highboy.gomantle.state

import com.highboy.gomantle.data.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameScreenState(
    private val _userGuess: MutableStateFlow<String> = MutableStateFlow(""),
    private val updateUser: () -> Boolean = {
        true
    },
    val userGuess: StateFlow<String> = _userGuess.asStateFlow(),
    private val _wordHistory: MutableStateFlow<List<Word>> = MutableStateFlow(emptyList()),
    val wordHistory: StateFlow<List<Word>> = _wordHistory.asStateFlow(),
    private val _selectedWord: MutableStateFlow<String> = MutableStateFlow(""),
    val selectedWord: StateFlow<String> = _selectedWord.asStateFlow(),
    private val _isWordDescriptionVisible: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val isWordDescriptionVisible: StateFlow<Boolean> = _isWordDescriptionVisible.asStateFlow(),
    private val _placeHolder: MutableStateFlow<String> = MutableStateFlow("Guess the word!"),
    val placeHolder: StateFlow<String> = _placeHolder.asStateFlow(),
    private val _isWarningDialogShowing: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val isWarningDialogShowing: StateFlow<Boolean> = _isWarningDialogShowing.asStateFlow(),
    private val _lastPrediction: MutableStateFlow<String> = MutableStateFlow(""),
    val lastPrediction: StateFlow<String> = _lastPrediction.asStateFlow(),
    private val _tryCount: MutableStateFlow<Int> = MutableStateFlow(0),
    val tryCount: StateFlow<Int> = _tryCount.asStateFlow()
)
