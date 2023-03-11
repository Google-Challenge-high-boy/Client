package com.highboy.gomantle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.UserProfileActivity
import com.highboy.gomantle.data.Word
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleGameScreen(
    modifier: Modifier = Modifier,
    viewModel: GomantleViewModel
) {
    Column(
        modifier = modifier
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
        ) {
            Box() {
                BasicTextField(
                    value = viewModel.userGuess,
                    onValueChange = { viewModel.updateUserGuessTextField(it) },
                    modifier = Modifier
                        .height(intrinsicSize = IntrinsicSize.Min)
                        .padding(20.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.checkUserGuess() }
                    ),
                )
                if(viewModel.userGuess.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .height(intrinsicSize = IntrinsicSize.Min)
                            .padding(20.dp),
                        text = "Guess the word")
                }

            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp),
        ) {
            GuessedWords(viewModel.guessedWords)

        }
    }
}

@Composable
fun GuessedWords(
    guessedWords: List<Word>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        itemsIndexed(guessedWords.toList()) { _, item ->
            GuessedWord(word = item)
        }
    }
}

@Composable
fun GuessedWord(
    word: Word
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            word.word,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}