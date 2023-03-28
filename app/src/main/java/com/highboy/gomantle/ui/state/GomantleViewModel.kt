package com.highboy.gomantle.ui.state

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.PrefRepository
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.network.GetSimilarityRequest
import com.highboy.gomantle.network.GetSimilarityResponse
import com.highboy.gomantle.network.RetrofitService.Companion.retrofitService
import com.highboy.gomantle.state.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GomantleViewModel() : ViewModel() {

    // shared-preferences
    private val pref = PrefRepository

    val gameScreenState = GameScreenState()
    val friendScreenState = FriendScreenState()
    val rankScreenState = RankScreenState()
    val myPageScreenState = MyPageScreenState()
    val globalState = GlobalState()

    // Infinite Scroll
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded: StateFlow<Boolean> = _isAllLoaded.asStateFlow()

    private val _userList = MutableStateFlow(emptyList<User>())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private var pageCount = 1

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    fun updateLastPrediction(word: String) {
        gameScreenState._lastPrediction.update { word }
        pref.putString(GlobalConstants.LAST_PREDICTION, lastPrediction.value)
    }

    fun updateWarningDialogVisibility(visibility: Boolean) {
        _isWarningDialogShowing.update { visibility }
    }
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
                delay(1500)
                _placeHolder.update { "Guess the word!" }
            }
            return
        }
        lateinit var guessedWord: Word
        viewModelScope.launch {
            guessedWord = Word(userGuess.value, getWordSimilarity(userGuess.value))
            Log.e("checkUserGuess", "${guessedWord.word} ${guessedWord.similarity}")
        }
    }

    // 단어의 유사도를 분석.
    private suspend fun getWordSimilarity(word: String): Float {

        var similarity = 0f
        val getSimilarityRequest = GetSimilarityRequest(userGuess.value, tryCount.value)
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
                    updateWarningDialogVisibility(true)
                }
                100f -> {
                    updateLastPrediction(word)
                    if(!checkIfGuessedWordExists(Word(word, similarity))) {
                        addGuessedWord(word, similarity)
                    }
                    pref.putListOfWord(GlobalConstants.WORD_HISTORY, guessedWords.value)
                    pref.putInt(GlobalConstants.TRY_COUNT, tryCount.value)
                }
                else -> {
                    updateLastPrediction(word)
                    similarity = getSimilarityResponse.similarity
                    if(!checkIfGuessedWordExists(Word(word, similarity))) {
                        addGuessedWord(word, similarity)
                    }
                    pref.putListOfWord(GlobalConstants.WORD_HISTORY, guessedWords.value)
                    pref.putInt(GlobalConstants.TRY_COUNT, tryCount.value)
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
        _guessedWords.update { guessedWords ->
            (guessedWords + Word(guessedWord, similarity)).sortedBy { it.similarity }
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

    fun loadSharedPreferences() {
        _userEmail.update { pref.getString(GlobalConstants.USER_EMAIL) }
        _serverTime.update { pref.getString(GlobalConstants.SERVER_TIME) }
        _lastPrediction.update { pref.getString(GlobalConstants.LAST_PREDICTION) }
        _guessedWords.update { pref.getListOfWord(GlobalConstants.WORD_HISTORY) }
        _tryCount.update { pref.getInt(GlobalConstants.TRY_COUNT) }
        _isFinished.update { pref.getBoolean(GlobalConstants.IS_FINISHED) }
        _isSignedIn.update { pref.getBoolean(GlobalConstants.IS_SIGNED_IN) }
    }

    fun updateIsSignedIn(): Boolean {
        return isSignedIn.value
    }

    fun updateIsSignInChecked(): Boolean {
        val isSignInChecked = pref.getString(GlobalConstants.USER_EMAIL)
        return (isSignInChecked != "")
    }
}