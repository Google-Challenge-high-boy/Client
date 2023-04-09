package com.highboy.gomantle.ui.state

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.highboy.gomantle.GlobalConstants
import com.highboy.gomantle.PrefRepository
import com.highboy.gomantle.data.FriendDataProvider
import com.highboy.gomantle.data.User
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.network.*
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
        date = globalMutableStateFlow._date.asStateFlow(),
        userName = globalMutableStateFlow._userName.asStateFlow(),
        userId = globalMutableStateFlow._userId.asStateFlow(),
        isSigningUp = globalMutableStateFlow._isSigningUp.asStateFlow(),
        isLoading = globalMutableStateFlow._isLoading.asStateFlow(),
        uiState = globalMutableStateFlow._uiState.asStateFlow(),
        userEmail = globalMutableStateFlow._userEmail.asStateFlow(),
        isFinished = globalMutableStateFlow._isFinished.asStateFlow(),
        isSignedIn = globalMutableStateFlow._isSignedIn.asStateFlow(),
        isSignInChecked = globalMutableStateFlow._isSignInChecked.asStateFlow()
    )

    private val gameScreenMutableStateFlow = GameScreenMutableStateFlow()
    val gameScreenStateFlow = GameScreenStateFlow(
        userGuess = gameScreenMutableStateFlow._userGuess.asStateFlow(),
        wordHistory = gameScreenMutableStateFlow._wordHistory.asStateFlow(),
        selectedWord = gameScreenMutableStateFlow._selectedWord.asStateFlow(),
        isWordDescriptionVisible = gameScreenMutableStateFlow._isWordDescriptionVisible.asStateFlow(),
        placeHolder = gameScreenMutableStateFlow._placeHolder.asStateFlow(),
        isWarningDialogShowing = gameScreenMutableStateFlow._isWarningDialogShowing.asStateFlow(),
        lastPrediction = gameScreenMutableStateFlow._lastPrediction.asStateFlow(),
        tryCount = gameScreenMutableStateFlow._tryCount.asStateFlow(),
        answerList = gameScreenMutableStateFlow._answerList.asStateFlow()
    )

    private val friendScreenMutableStateFlow = FriendScreenMutableStateFlow()
    val friendScreenStateFlow = FriendScreenStateFlow(
        friendId = friendScreenMutableStateFlow._friendId.asStateFlow(),
        placeHolder = friendScreenMutableStateFlow._placeHolder.asStateFlow(),
        friendsList = friendScreenMutableStateFlow._friendsList.asStateFlow(),
        isSnackBarShowing = friendScreenMutableStateFlow._isSnackBarShowing.asStateFlow(),
        snackBarText = friendScreenMutableStateFlow._snackBarText.asStateFlow()
    )

    private val rankScreenMutableStateFlow = RankScreenMutableStateFlow()
    val rankScreenStateFlow = RankScreenStateFlow(
        selectedYear = rankScreenMutableStateFlow._selectedYear.asStateFlow(),
        selectedMonth = rankScreenMutableStateFlow._selectedMonth.asStateFlow(),
        selectedDayOfMonth = rankScreenMutableStateFlow._selectedDayOfMonth.asStateFlow(),
        myRank = rankScreenMutableStateFlow._myRank.asStateFlow(),
        allRank = rankScreenMutableStateFlow._allRank.asStateFlow()
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

    /**
     * Global
     */

    fun signIn() {
        val signInRequest = SignInRequest(pref.getString(GlobalConstants.USER_EMAIL))
        viewModelScope.launch {
            val signInResponse: SignInResponse = try {
                retrofitService.signIn(signInRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                SignInResponse(300, null, null)
            } catch(e: HttpException) {
                e.printStackTrace()
                SignInResponse(300, null, null)
            }
            Log.e("signIn", signInResponse.status.toString())
            Log.e("signIn", signInResponse.date.toString())
            Log.e("signIn", signInResponse.user.toString())
            if(signInResponse.status == 200) {
                globalMutableStateFlow._date.update { signInResponse.date ?: ""}
                globalMutableStateFlow._userId.update { signInResponse.user?.id ?: -1}
                globalMutableStateFlow._userName.update { signInResponse.user?.name ?: "" }
                globalMutableStateFlow._isSignInChecked.update { true }
                globalMutableStateFlow._isSignedIn.update { true }
            } else {
                globalMutableStateFlow._isSigningUp.update { true }
            }
        }
    }

    fun signUp(nickname: String) {
        val signUpRequest = SignUpRequest(pref.getString(GlobalConstants.USER_EMAIL), nickname)
        viewModelScope.launch {
            val signUpResponse: SignUpResponse = try {
                retrofitService.signUp(signUpRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                SignUpResponse(400)
            } catch(e: HttpException) {
                e.printStackTrace()
                SignUpResponse(400)
            }
            Log.e("signUp", signUpResponse.status.toString())
            if(signUpResponse.status == 200) {
                globalMutableStateFlow._isSigningUp.update { false }
            } else {
                globalMutableStateFlow._isSigningUp.update { false }
                globalMutableStateFlow._isSignInChecked.update { false }
            }
        }
    }

    fun updateCurrentView(viewType: ViewType) {
        globalMutableStateFlow._uiState.update { viewType }
    }

    fun launchLoading() {
        viewModelScope.launch {
            delay(3000)
            globalMutableStateFlow._isLoading.update { false }
        }
    }

    fun updateEmail(email: String) {
        globalMutableStateFlow._userEmail.update { email }
    }

    /**
     * Game Screen
     */

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

    fun giveUp() {
        viewModelScope.launch {
            val giveUpResponse: GiveUpResponse = try {
                retrofitService.getAnswerList()
            } catch(e: IOException) {
                e.printStackTrace()
                GiveUpResponse(mapOf("" to ""))
            } catch(e: HttpException) {
                e.printStackTrace()
                GiveUpResponse(mapOf("" to ""))
            }
            gameScreenMutableStateFlow._answerList.update {
                val list1 = giveUpResponse.answerList.toList()
                val list2 = list1.sortedByDescending { it.second.toFloat() }
                val map1 = list2.toMap()
                map1
            }
            gameScreenMutableStateFlow._wordHistory.update {
                gameScreenStateFlow.answerList.value.map {
                    Word(it.key, it.value.toFloat())
                }
            }
            Log.e("giveUp", gameScreenStateFlow.answerList.value.toString())
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
                retrofitService.getSimilarity(mapOf("userId" to globalStateFlow.userId.value), getSimilarityRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                GetSimilarityResponse(0f, mapOf())
            } catch(e: HttpException) {
                e.printStackTrace()
                GetSimilarityResponse(0f, mapOf())
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
                    gameScreenMutableStateFlow._tryCount.update {
                        it + 1
                    }
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
            (guessedWords + Word(guessedWord, similarity)).sortedByDescending { it.similarity }
        }
    }

    // friend screen methods
    fun loadMore() {
        viewModelScope.launch {
            _isLoading.update { true }
            delay(1000)
            val items = arrayListOf<User>()
            repeat(20) {
                items.add(FriendDataProvider.friendsList[it])
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

    /**
     * Friend Screen
     */

    fun followUp(userId: Long) {
        val followUpRequest = FollowUpRequest(userId)
        viewModelScope.launch {
            val followUpResponse = try {
                retrofitService.followUp(mapOf<String, Long>("userId" to globalStateFlow.userId.value), followUpRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                FollowUpResponse(false)
            } catch(e: HttpException) {
                e.printStackTrace()
                FollowUpResponse(false)
            }
        }
    }

    // 단어 입력 후 Done을 눌렀을 때.

    private fun getFriends() {
        viewModelScope.launch {
            val getFriendsResponse = try {
                retrofitService.getFriends(mapOf("userId" to globalStateFlow.userId.value))
            } catch(e: IOException) {
                e.printStackTrace()
                listOf<GetFriendsResponse>()
            } catch(e: HttpException) {
                e.printStackTrace()
                listOf<GetFriendsResponse>()
            }
        }
    }

    fun updateFriendIdTextField(friendId: String) {
        friendScreenMutableStateFlow._friendId.update {
            friendId
        }
    }

    fun checkFriendId() {
        if(friendScreenStateFlow.friendId.value == "") {
            viewModelScope.launch {
                friendScreenMutableStateFlow._placeHolder.update { "Please enter valid Id!" }
                delay(1500)
                friendScreenMutableStateFlow._placeHolder.update { "friend Id" }
            }
            return
        }
        for(user in FriendDataProvider.friendsList) {
            if(user.userId == friendScreenStateFlow.friendId.value.toLong()) {
                friendScreenMutableStateFlow._friendsList.update {
                    it + user
                }
                viewModelScope.launch {
                    friendScreenMutableStateFlow._snackBarText.update { "Added successfully" }
                    friendScreenMutableStateFlow._isSnackBarShowing.update { true }
                    delay(3000)
                    friendScreenMutableStateFlow._isSnackBarShowing.update { false }
                }
                return
            }
        }
        viewModelScope.launch {
            friendScreenMutableStateFlow._snackBarText.update { "There is no such user." }
            friendScreenMutableStateFlow._isSnackBarShowing.update { true }
            delay(3000)
            friendScreenMutableStateFlow._isSnackBarShowing.update { false }
        }
    }

    /**
     * Rank Screen
     */
    fun loadRankInfo() {

        val getRankRequest = GetRankRequest("")
        viewModelScope.launch {
            val getRankResponse: GetRankResponse = try {
                retrofitService.getRank(getRankRequest)
            } catch(e: IOException) {
                e.printStackTrace()
                GetRankResponse(0, listOf(""))
            } catch(e: HttpException) {
                e.printStackTrace()
                GetRankResponse(0, listOf(""))
            }
            rankScreenMutableStateFlow._myRank.update { getRankResponse.myRank }
            rankScreenMutableStateFlow._allRank.update { getRankResponse.allRank }
        }
    }

    fun updateSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        rankScreenMutableStateFlow._selectedYear.update { year }
        rankScreenMutableStateFlow._selectedMonth.update { month }
        rankScreenMutableStateFlow._selectedDayOfMonth.update { dayOfMonth }
    }
    /**
     * MyPage Screen
     */
    fun loadSharedPreferences() {
        globalMutableStateFlow._userEmail.update { pref.getString(GlobalConstants.USER_EMAIL) }
        gameScreenMutableStateFlow._lastPrediction.update { pref.getString(GlobalConstants.LAST_PREDICTION) }
        gameScreenMutableStateFlow._wordHistory.update { pref.getListOfWord(GlobalConstants.WORD_HISTORY) }
        gameScreenMutableStateFlow._tryCount.update { pref.getInt(GlobalConstants.TRY_COUNT) }
        globalMutableStateFlow._isFinished.update { pref.getBoolean(GlobalConstants.IS_FINISHED) }
        globalMutableStateFlow._isSignedIn.update {
            (pref.getString(GlobalConstants.USER_EMAIL) != "") && (pref.getString(GlobalConstants.USER_EMAIL) != "guest")
        }
        if(globalMutableStateFlow._isSignedIn.value) {
            signIn()
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