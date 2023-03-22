package com.highboy.gomantle.ui.state

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.network.GomantleApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class GomantleViewModel() : ViewModel() {

    // network
    private val BASE_URL = "https://jsonplaceholder.typicode.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GomantleApiService by lazy {
        retrofit.create(GomantleApiService::class.java)
    }

    private val _uiState = MutableStateFlow(GomantleUiState())
    val uiState: StateFlow<GomantleUiState> = _uiState

    var userGuess by mutableStateOf("")
    var guessedWords: MutableList<Word> = mutableStateListOf()
    var userList: List<User> = mutableStateListOf()
    var isWordDescriptionVisible by mutableStateOf(false)
    var selectedWord: String = ""

    // game
    //
    fun showWordDescription(word: String) {
        isWordDescriptionVisible = true
        selectedWord = word
    }

    fun getDescription(): String {
        return selectedWord
    }

    fun hideWordDescription() {
        isWordDescriptionVisible = false
    }

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

    private fun getWordSimilarity(word: String): Double {
        return 0.0
    }

    // 같은 단어를 입력했었는지 확인.
    private fun checkIfGuessedWordExists(word: Word): Boolean {
        for(existingWord in guessedWords) {
            if(word.word == existingWord.word) return true
        }
        return false
    }

    // 단어가 존재하지 않으면 단어를 추가.
    private fun addGuessedWord(guessedWord: String) {
        guessedWords.add(Word(guessedWord, getWordSimilarity(guessedWord)))
    }

    private fun getUsers() {
        viewModelScope.launch {
            userList = try {
                retrofitService.getUsers()
            } catch(e: IOException) {
                Log.d("", "")
                emptyList()
            } catch(e: HttpException) {
                Log.d("", "")
                emptyList()
            }
        }
    }

}