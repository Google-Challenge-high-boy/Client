package com.highboy.gomantle.ui.state

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.highboy.gomantle.GomantleApplication
import com.highboy.gomantle.data.GomantleRepository
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GomantleViewModel(private val gomantleRepository: GomantleRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GomantleUiState())
    val uiState: StateFlow<GomantleUiState> = _uiState

    var userGuess by mutableStateOf("")
    var guessedWords: MutableList<Word> = mutableStateListOf()
    var userList: List<User> = mutableStateListOf()

    fun updateCurrentView(viewType: ViewType) {
        _uiState.update {
            it.copy(
                currentViewType = viewType
            )
        }
    }

    init {
        getUsers()
    }

    fun updateUserGuessTextField(guessedWord: String) {
        userGuess = guessedWord
    }

    // 단어 입력 후 Done을 눌렀을 때.
    fun checkUserGuess() {
        val guessedWord = Word(userGuess, getWordSimilarity(userGuess))

        if(!checkIfGuessedWordExists(guessedWord)) {
            addGuessedWord(userGuess)
        }
    }

    fun getWordSimilarity(word: String): Double {
        return 0.0
    }

    // 같은 단어를 입력했었는지 확인.
    fun checkIfGuessedWordExists(word: Word): Boolean {
        for(existingWord in guessedWords) {
            if(word.word == existingWord.word) return true
        }
        return false
    }

    // 단어가 존재하지 않으면 단어를 추가.
    fun addGuessedWord(guessedWord: String) {
        guessedWords.add(Word(guessedWord, getWordSimilarity(guessedWord)))
    }

    fun getUsers() {
        viewModelScope.launch {
            userList = try {
                gomantleRepository.getUsers()
            } catch(e: IOException) {
                Log.d("", "")
                emptyList()
            } catch(e: HttpException) {
                Log.d("", "")
                emptyList()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GomantleApplication)
                val gomantleRepository = application.container.gomantleRepository
                GomantleViewModel(gomantleRepository = gomantleRepository)
            }
        }
    }
}