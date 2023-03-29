package com.highboy.gomantle.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.UserProfileActivity
import com.highboy.gomantle.data.User
import com.highboy.gomantle.ui.state.GomantleViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun GomantleRankScreen(
    modifier: Modifier = Modifier,
    viewModel: GomantleViewModel = viewModel(),
    loadMore: () -> Unit
) {
    val context = LocalContext.current
    val users = viewModel.userList.collectAsState().value

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        modifier = modifier
    ) {
        itemsIndexed(users) { idx, user ->
            UserListItem(user = user, idx = idx) {
                startActivity(context, UserProfileActivity.newIntent(context, it), null)
            }
        }
    }

    InfiniteListHandler(listState) {
        loadMore()
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