package com.highboy.gomantle.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.highboy.gomantle.R
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme

@Composable
fun GomantleGameScreen(
    paddingValues: PaddingValues,
    viewModel: GomantleViewModel = viewModel()
) {
    Surface(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Column() {
            WordInputBox()
            WordHistoryBox()
        }
    }
}

// 단어를 입력하는 박스
@Composable
fun WordInputBox(
    viewModel: GomantleViewModel = viewModel(),
) {
    val userGuess = viewModel.gameScreenStateFlow.userGuess.collectAsState().value
    GomantleTheme() {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .clip(shape)
                            .clickable() {
                                viewModel.checkUserGuess()
                                viewModel.updateUserGuessTextField("")
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = ""
                            )
                        }
                    }
                }
                BasicTextField(
                    value = userGuess,
                    onValueChange = { viewModel.updateUserGuessTextField(it) },
                    modifier = Modifier
                        .height(intrinsicSize = IntrinsicSize.Min)
                        .padding(start = 12.dp, top = 12.dp, end = 36.dp, bottom = 12.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.checkUserGuess()
                            viewModel.updateUserGuessTextField("")
                        }
                    ),
                    singleLine = true
                )
                if (viewModel.gameScreenStateFlow.userGuess.collectAsState().value.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .height(intrinsicSize = IntrinsicSize.Min)
                            .padding(12.dp),
                        text = viewModel.gameScreenStateFlow.placeHolder.collectAsState().value
                    )
                }
            }
        }
    }
}

// 지금까지 입력한 단어들이 보여지는 박스.
@Composable
fun WordHistoryBox(
    viewModel: GomantleViewModel = viewModel()
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp),
        ) {
            Text(
                text = "Last prediction: " + viewModel.gameScreenStateFlow.lastPrediction.collectAsState().value,
                modifier = Modifier
                    .padding(12.dp)
            )
            Text(
                text = "Try count: " + viewModel.gameScreenStateFlow.tryCount.collectAsState().value,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color(0.5f, 0.5f, 0.5f))
                )
            }

            Box() {
                GuessedWords()
                WordDescription()
            }
        }
        if(viewModel.gameScreenStateFlow.isWarningDialogShowing.collectAsState().value) {
            WarningDialog(onDismissRequest = viewModel::updateWarningDialogVisibility)
        }
    }
}

// 단어 history의 리스트
@Composable
fun GuessedWords(
    viewModel: GomantleViewModel = viewModel()
) {
    val wordHistory = viewModel.gameScreenStateFlow.wordHistory.collectAsState().value
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) {
        itemsIndexed(wordHistory.toList()) { _, item ->
            GuessedWord(
                word = item,
                showWordDescription = {
                    viewModel.showWordDescription(it)
                }
            )
        }
    }
}

// 단어 히스토리 개별 아이템
@Composable
fun GuessedWord(
    word: Word,
    showWordDescription: (String) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
            .clickable {
                showWordDescription(word.word)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = word.word,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
            Text(
                text = word.similarity.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

// 개별 히스토리 단어 터치시 표시되는 설명 박스
@Composable
fun WordDescription(
    viewModel: GomantleViewModel = viewModel()
) {
    AnimatedVisibility(
        visible = viewModel.gameScreenStateFlow.isWordDescriptionVisible.collectAsState().value,
        modifier = Modifier,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                    viewModel.hideWordDescription()
                }
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = viewModel.getDescription()
            )
            WordDictionaryWebView(viewModel.getDescription())
        }
    }
}

@Composable
fun WarningDialog(
    onDismissRequest: (Boolean) -> Unit,
    properties: DialogProperties = DialogProperties()
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp)
            .clickable {
                onDismissRequest(false)
            }
    ) {
        Text("No Word find")
    }
}

@Composable
fun WordDictionaryWebView(
    word: String
) {
    val state = rememberWebViewState(url = "https://www.oxfordlearnersdictionaries.com/definition/english/$word")
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(12.dp)
    ) {
        WebView(
            modifier = Modifier,
            state = state
        )
    }
}