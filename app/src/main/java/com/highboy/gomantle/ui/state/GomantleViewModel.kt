package com.highboy.gomantle.ui.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.network.GetSimilarityRequest
import com.highboy.gomantle.network.GetSimilarityResponse
import com.highboy.gomantle.network.GomantleApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.io.IOException


class GomantleViewModel() : ViewModel() {



    // network
    private val BASE_URL = "https://47fa3e21-8401-4b7e-910a-6dd2da2d6ea0.mock.pstmn.io"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GomantleApiService by lazy {
        retrofit.create(GomantleApiService::class.java)
    }

    // login
    // 로그인 여부 확인 했나 안했나
    private val _isSignInChecked = MutableStateFlow(false)
    val isSignInChecked: StateFlow<Boolean> = _isSignInChecked

    // 로그인 했나 안했나
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

    // UI
    // Game / Friend / Rank / MyPage 구분
    private val _uiState = MutableStateFlow(GomantleUiState())
    val uiState: StateFlow<GomantleUiState> = _uiState

    // 지금까지 입력한 단어 히스토리
    private val _guessedWords = MutableStateFlow(emptyList<Word>())
    val guessedWords: StateFlow<List<Word>> = _guessedWords

    // 현재 입력중인 단어
    private val _userGuess = MutableStateFlow("")
    val userGuess: StateFlow<String> = _userGuess

    // 히스토리에서 선택한 단어
    private val _selectedWord = MutableStateFlow("")
    val selectedWord: StateFlow<String> = _selectedWord

    // 단어의 설명 뷰가 보이나 안보이나
    private val _isWordDescriptionVisible = MutableStateFlow(false)
    val isWordDescriptionVisible: StateFlow<Boolean> = _isWordDescriptionVisible

    private val _placeHolder = MutableStateFlow("Guess the word!")
    val placeHolder: StateFlow<String> = _placeHolder

    fun loadMore() {
        viewModelScope.launch {

            _isLoading.update { true }
            delay(1000)
            val items = arrayListOf<User>()
            repeat(20) {
                items.add(User("example@gmail.com", "User$it"))
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

    init {

    }



    // game

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

    fun updateUserGuessTextField(guessedWord: String) {
        _userGuess.update {
            guessedWord
        }
    }

    // 단어 입력 후 Done을 눌렀을 때.
    fun checkUserGuess() {
        if(userGuess.value == "") {
            viewModelScope.launch {
                _placeHolder.update { "Please enter at least one word!" }
                delay(3000)
                _placeHolder.update { "Guess the word!" }
            }
            return
        }
        lateinit var guessedWord: Word
        viewModelScope.launch {
            guessedWord = Word(userGuess.value, getWordSimilarity(userGuess.value))
            if(!checkIfGuessedWordExists(guessedWord)) {
                addGuessedWord(guessedWord.word, guessedWord.similarity)
            }
            Log.e("checkUserGuess", "${guessedWord.word} ${guessedWord.similarity}")
        }
    }

    // 단어의 유사도를 분석.
    private suspend fun getWordSimilarity(word: String): Float {

        var similarity = 0f
        val getSimilarityRequest = GetSimilarityRequest(userGuess.value, GlobalConstants.TRY_COUNT)
        viewModelScope.launch {
            val getSimilarityResponse: GetSimilarityResponse = try {
                retrofitService.getSimilarity(getSimilarityRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                GetSimilarityResponse(0, 0f, mapOf())
            } catch(e: HttpException) {
                e.printStackTrace()
                GetSimilarityResponse(0, 0f, mapOf())
            }
            similarity = getSimilarityResponse.similarity
            when(similarity) {
                200f -> {

                }
                100f -> {

                }
                else -> {
                    similarity = getSimilarityResponse.similarity
                }
            }
            Log.e("similarity", "${getSimilarityResponse.similarity}")
            Log.e("answerList", "${getSimilarityResponse.answerList.entries}")
        }.join()
        return similarity
    }

    // 같은 단어를 입력했었는지 확인.
    private fun checkIfGuessedWordExists(word: Word): Boolean {
        for(existingWord in guessedWords.value) {
            if(word.word == existingWord.word) return true
        }
        return false
    }

    // 단어가 존재하지 않으면 단어를 추가.
    private fun addGuessedWord(guessedWord: String, similarity: Float) {
        _guessedWords.update {
            it + Word(guessedWord, similarity)
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
//            _userList.update {
//                try {
//                    retrofitService.getUsers()
//                } catch(e: IOException) {
//                    Log.d("", "")
//                    emptyList()
//                } catch(e: HttpException) {
//                    Log.d("", "")
//                    emptyList()
//                }
//            }
        }
    }

}