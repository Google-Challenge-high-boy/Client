package com.highboy.gomantle.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.ViewModel
import com.highboy.gomantle.R
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.ui.state.GomantleUiState
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleHomeScreen(
    gomantleUiState: GomantleUiState,
    onTabPressed: (ViewType) -> Unit,
    modifier: Modifier,
    viewModel: GomantleViewModel
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            viewType = ViewType.Game,
            icon = R.drawable.stadia_controller_48px,
            text = "Game"
        ),
        NavigationItemContent(
            viewType = ViewType.Friends,
            icon = R.drawable.group_48px,
            text = "Friends"
        ),NavigationItemContent(
            viewType = ViewType.Rank,
            icon = R.drawable.military_tech_48px,
            text = "Rank"
        ),NavigationItemContent(
            viewType = ViewType.MyPage,
            icon = R.drawable.account_circle_48px,
            text = "MyPage"
        )
    )
    GomantleAppContent(
        gomantleUiState = gomantleUiState,
        navigationItemContentList = navigationItemContentList,
        onTabPressed = onTabPressed,
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
private fun GomantleAppContent(
    gomantleUiState: GomantleUiState,
    navigationItemContentList: List<NavigationItemContent>,
    onTabPressed: (ViewType) -> Unit,
    modifier: Modifier,
    viewModel: GomantleViewModel
) {
    Column() {
        GomantleOnlyContentView(
            modifier = Modifier.weight(13f),
            currentTab = gomantleUiState.currentViewType,
            viewModel = viewModel
        )
        GomantleBottomNavigationBar(
            navigationItemContentList = navigationItemContentList,
            modifier = Modifier.weight(1f),
            onTabPressed = onTabPressed,
            currentTab = gomantleUiState.currentViewType
        )
    }
}

@Composable
fun GomantleOnlyContentView(
    modifier: Modifier = Modifier,
    currentTab: ViewType,
    viewModel: GomantleViewModel
) {
    when(currentTab) {
        ViewType.Game -> {
            GomantleGameScreen(
                modifier = modifier,
                viewModel = viewModel
            )
        }
        ViewType.Friends -> {
            GomantleFriendsScreen(
                modifier = modifier
            )
        }
        ViewType.Rank -> {
            GomantleRankScreen(
                modifier = modifier,
                viewModel = viewModel
            )
        }
        ViewType.MyPage -> {
            GomantleMyPageScreen(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun GomantleBottomNavigationBar(
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
    onTabPressed: (ViewType) -> Unit = {},
    currentTab: ViewType
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(255, 200, 155)
    ) {
        navigationItemContentList.forEachIndexed { _, navItem ->
            NavigationBarItem(
                selected = currentTab == navItem.viewType,
                onClick = {
                    onTabPressed(navItem.viewType)
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = navItem.icon),
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .fillMaxHeight(0.6f)
                            .fillMaxWidth(0.6f)
                    )
                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color(255, 0, 0),
//                    indicatorColor = Color(0, 0, 255)
//                )
            )
        }
    }
}

private data class NavigationItemContent(
    val viewType: ViewType,
    @DrawableRes
    val icon: Int,
    val text: String
)