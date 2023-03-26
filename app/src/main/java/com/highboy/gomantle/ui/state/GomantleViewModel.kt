package com.highboy.gomantle.ui.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.network.GomantleApiService
import kotlinx.coroutines.delay
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

    // login
    private val _isSignInChecked = MutableStateFlow(false)
    val isSignInChecked: StateFlow<Boolean> = _isSignInChecked

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn

    // Infinite Scroll
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded: StateFlow<Boolean> = _isAllLoaded

    private val _userList = MutableStateFlow(emptyList<User>())
    val userList: StateFlow<List<User>> = _userList

    private var pageCount = 1

    fun loadMore() {
        viewModelScope.launch {

            _isLoading.update { true }
            delay(1000)
            val items = arrayListOf<User>()
            repeat(20) {
                items.add(User(1, "User$it"))
            }
            _userList.update {
                _userList.value + items
            }
            _isLoading.update { false }
            pageCount++
            if(pageCount > 10) {
                _isAllLoaded.update { true }
            }
        }
    }

    //

    private val _uiState = MutableStateFlow(GomantleUiState())
    val uiState: StateFlow<GomantleUiState> = _uiState

    private val _guessedWords = MutableStateFlow(emptyList<Word>())
    val guessedWords: StateFlow<List<Word>> = _guessedWords

    private val _userGuess = MutableStateFlow("")
    val userGuess: StateFlow<String> = _userGuess

    private val _selectedWord = MutableStateFlow("")
    val selectedWord: StateFlow<String> = _selectedWord

    private val _isWordDescriptionVisible = MutableStateFlow(false)
    val isWordDescriptionVisible: StateFlow<Boolean> = _isWordDescriptionVisible

    // game
    //
    fun showWordDescription(word: String) {
        _isWordDescriptionVisible.update { true }
        _selectedWord.update { word }
    }

    fun getDescription(): String {
        return selectedWord.value
    }

    fun hideWordDescription() {
        _isWordDescriptionVisible.update { false }
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
        _userGuess.update {
            guessedWord
        }
    }

    // 단어 입력 후 Done을 눌렀을 때.
    fun checkUserGuess() {
        val guessedWord = Word(userGuess.value, getWordSimilarity(userGuess.value))

        if(!checkIfGuessedWordExists(guessedWord)) {
            addGuessedWord(userGuess.value)
        }
    }

    private fun getWordSimilarity(word: String): Float {
        return 0f
    }

    // 같은 단어를 입력했었는지 확인.
    private fun checkIfGuessedWordExists(word: Word): Boolean {
        for(existingWord in guessedWords.value) {
            if(word.word == existingWord.word) return true
        }
        return false
    }

    // 단어가 존재하지 않으면 단어를 추가.
    private fun addGuessedWord(guessedWord: String) {
        _guessedWords.update {
            it + Word(guessedWord, getWordSimilarity(guessedWord))
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            _userList.update {
                try {
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

}