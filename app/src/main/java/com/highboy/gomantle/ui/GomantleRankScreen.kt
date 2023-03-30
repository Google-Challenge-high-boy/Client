package com.highboy.gomantle.ui

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.UserProfileActivity
import com.highboy.gomantle.data.User
import com.highboy.gomantle.ui.state.GomantleViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GomantleRankScreen(
    paddingValues: PaddingValues,
    viewModel: GomantleViewModel = viewModel(),
    loadMore: () -> Unit
) {
    val context = LocalContext.current
    val users = viewModel.userList.collectAsState().value

    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier
            .padding(paddingValues),
    ) {
        Column {
            GomantleDatePickerDialog()
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                state = listState
            ) {
                itemsIndexed(users) { idx, user ->
                    UserListItem(user = user, idx = idx) {
                        startActivity(context, UserProfileActivity.newIntent(context, it), null)
                    }
                }
            }
        }
    }

    InfiniteListHandler(listState) {
        loadMore()
    }
}
@ExperimentalMaterial3Api
@Composable
fun GomantleDatePickerDialog(
    viewModel: GomantleViewModel = viewModel()
) {
    val calendar = Calendar.getInstance()
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        {
            _, year, month, dayOfMonth ->
            viewModel.updateSelectedDate(year, month, dayOfMonth)
            Log.e("datepicker", "$year, $month, $dayOfMonth")
//            viewModel.getRank(year, month, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                datePickerDialog.show()
            }
        ) {
            Text(viewModel.rankScreenStateFlow.selectedDate.collectAsState().value)
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    idx: Int,
    navigateToProfile: (User) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp)
            .clickable {
                navigateToProfile(user)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Column() {
                Text(text = "user.userName$idx", style = typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(text = "VIEW DETAIL", style = typography.bodyMedium)
            }
        }
    }
}

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }
    }
}