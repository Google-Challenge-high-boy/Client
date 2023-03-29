package com.highboy.gomantle.ui.state

import android.provider.Settings.Global
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

    private val globalMutableStateFlow = GlobalMutableStateFlow()
    val globalStateFlow = GlobalStateFlow(
        globalMutableStateFlow._uiState.asStateFlow(),
        globalMutableStateFlow._userEmail.asStateFlow(),
        globalMutableStateFlow._isFinished.asStateFlow(),
        globalMutableStateFlow._isSignedIn.asStateFlow(),
        globalMutableStateFlow._isSignInChecked.asStateFlow()
    )

    private val gameScreenMutableStateFlow = GameScreenMutableStateFlow()
    val gameScreenStateFlow = GameScreenStateFlow(
        gameScreenMutableStateFlow._userGuess.asStateFlow(),
        gameScreenMutableStateFlow._wordHistory.asStateFlow(),
        gameScreenMutableStateFlow._selectedWord.asStateFlow(),
        gameScreenMutableStateFlow._isWordDescriptionVisible.asStateFlow(),
        gameScreenMutableStateFlow._placeHolder.asStateFlow(),
        gameScreenMutableStateFlow._isWarningDialogShowing.asStateFlow(),
        gameScreenMutableStateFlow._lastPrediction.asStateFlow(),
        gameScreenMutableStateFlow._tryCount.asStateFlow()
    )
    private val friendScreenMutableStateFlow = FriendScreenMutableStateFlow()
    val friendScreenStateFlow = FriendScreenStateFlow(
        friendScreenMutableStateFlow.tmp
    )
    private val rankScreenMutableStateFlow = RankScreenMutableStateFlow()
    val rankScreenStateFlow = RankScreenStateFlow(
        rankScreenMutableStateFlow.tmp
    )
    private val myPageScreenMutableStateFlow = MyPageScreenMutableStateFlow()
    val myPageScreenStateFlow = MyPageScreenStateFlow(
        myPageScreenMutableStateFlow.tmp
    )

    // Infinite Scroll
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded: StateFlow<Boolean> = _isAllLoaded.asStateFlow()

    private val _userList = MutableStateFlow(emptyList<User>())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private var pageCount = 1

    // global methods
    fun updateCurrentView(viewType: ViewType) {
        globalMutableStateFlow._uiState.update { viewType }
    }

    // game screen methods
    fun updateLastPrediction(word: String) {
        gameScreenMutableStateFlow._lastPrediction.update { word }
        pref.putString(GlobalConstants.LAST_PREDICTION, gameScreenStateFlow.lastPrediction.value)
    }

    fun updateWarningDialogVisibility(visibility: Boolean) {
        gameScreenMutableStateFlow._isWarningDialogShowing.update { visibility }
    }

    fun showWordDescription(word: String) {
        gameScreenMutableStateFlow._isWordDescriptionVisible.update { true }
        gameScreenMutableStateFlow._selectedWord.update { word }
    }

    fun getDescription(): String {
        return gameScreenStateFlow.selectedWord.value
    }

    fun hideWordDescription() {
        gameScreenMutableStateFlow._isWordDescriptionVisible.update { false }
    }

    fun updateUserGuessTextField(guessedWord: String) {
        gameScreenMutableStateFlow._userGuess.update {
            guessedWord
        }
    }

    fun checkUserGuess() {
        if(gameScreenStateFlow.userGuess.value == "") {
            viewModelScope.launch {
                gameScreenMutableStateFlow._placeHolder.update { "Please enter at least one word!" }
                delay(1500)
                gameScreenMutableStateFlow._placeHolder.update { "Guess the word!" }
            }
            return
        }
        lateinit var guessedWord: Word
        viewModelScope.launch {
            guessedWord = Word(gameScreenStateFlow.userGuess.value, getWordSimilarity(gameScreenStateFlow.userGuess.value))
            Log.e("checkUserGuess", "${guessedWord.word} ${guessedWord.similarity}")
        }
    }

    // 단어의 유사도를 분석.
    private suspend fun getWordSimilarity(word: String): Float {

        var similarity = 0f
        val getSimilarityRequest = GetSimilarityRequest(gameScreenStateFlow.userGuess.value, gameScreenStateFlow.tryCount.value)
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
                    pref.putListOfWord(GlobalConstants.WORD_HISTORY, gameScreenStateFlow.wordHistory.value)
                    pref.putInt(GlobalConstants.TRY_COUNT, gameScreenStateFlow.tryCount.value)
                }
                else -> {
                    updateLastPrediction(word)
                    similarity = getSimilarityResponse.similarity
                    if(!checkIfGuessedWordExists(Word(word, similarity))) {
                        addGuessedWord(word, similarity)
                    }
                    pref.putListOfWord(GlobalConstants.WORD_HISTORY, gameScreenStateFlow.wordHistory.value)
                    pref.putInt(GlobalConstants.TRY_COUNT, gameScreenStateFlow.tryCount.value)
                }
            }
            Log.e("similarity", "${getSimilarityResponse.similarity}")
            Log.e("answerList", "${getSimilarityResponse.answerList.entries}")
        }.join()
        return similarity
    }

    // 같은 단어를 입력했었는지 확인.
    private fun checkIfGuessedWordExists(word: Word): Boolean {
        for(existingWord in gameScreenStateFlow.wordHistory.value) {
            if(word.word == existingWord.word) return true
        }
        return false
    }

    // 단어가 존재하지 않으면 단어를 추가.
    private fun addGuessedWord(guessedWord: String, similarity: Float) {
        gameScreenMutableStateFlow._wordHistory.update { guessedWords ->
            (guessedWords + Word(guessedWord, similarity)).sortedBy { it.similarity }
        }
    }

    // friend screen methods
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

    // game



    // 단어 입력 후 Done을 눌렀을 때.


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
        globalMutableStateFlow._userEmail.update { pref.getString(GlobalConstants.USER_EMAIL) }
        gameScreenMutableStateFlow._lastPrediction.update { pref.getString(GlobalConstants.LAST_PREDICTION) }
        gameScreenMutableStateFlow._wordHistory.update { pref.getListOfWord(GlobalConstants.WORD_HISTORY) }
        gameScreenMutableStateFlow._tryCount.update { pref.getInt(GlobalConstants.TRY_COUNT) }
        globalMutableStateFlow._isFinished.update { pref.getBoolean(GlobalConstants.IS_FINISHED) }
        globalMutableStateFlow._isSignedIn.update {
            (pref.getString(GlobalConstants.USER_EMAIL) != "") && (pref.getString(GlobalConstants.USER_EMAIL) != "guest")
        }
        globalMutableStateFlow._isSignInChecked.update {
            pref.getString(GlobalConstants.USER_EMAIL) != ""
        }
    }

    fun updateIsSignedIn(signedIn: Boolean) {
        globalMutableStateFlow._isSignedIn.update { signedIn }
    }

    fun updateIsSignInChecked(checked: Boolean) {
        globalMutableStateFlow._isSignInChecked.update { checked }
    }
}