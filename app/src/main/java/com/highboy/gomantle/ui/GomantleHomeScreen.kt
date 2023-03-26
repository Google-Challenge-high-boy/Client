package com.highboy.gomantle.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.highboy.gomantle.R
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.ui.state.GomantleUiState
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleHomeScreen(
    gomantleUiState: GomantleUiState,
    onTabPressed: (ViewType) -> Unit,
    modifier: Modifier,
    viewModel: GomantleViewModel,
    isSignedIn: Boolean,
    startSignIn: () -> Unit
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
        viewModel = viewModel,
        isSignedIn = isSignedIn,
        startSignIn = startSignIn
    )
}

@Composable
private fun GomantleAppContent(
    gomantleUiState: GomantleUiState,
    navigationItemContentList: List<NavigationItemContent>,
    onTabPressed: (ViewType) -> Unit,
    modifier: Modifier,
    viewModel: GomantleViewModel,
    isSignedIn: Boolean,
    startSignIn: () -> Unit
) {
    Column() {
        GomantleOnlyContentView(
            modifier = Modifier.weight(13f),
            currentTab = gomantleUiState.currentViewType,
            viewModel = viewModel,
            isSignedIn = isSignedIn,
            startSignIn = startSignIn
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
    viewModel: GomantleViewModel,
    isSignedIn: Boolean,
    startSignIn: () -> Unit
) {
    val isLoading = viewModel.isLoading.collectAsState().value
    val isAllLoaded = viewModel.isAllLoaded.collectAsState().value

    when(currentTab) {
        ViewType.Game -> {
            GomantleGameScreen(
                modifier = modifier,
                viewModel = viewModel
            )
        }
        ViewType.Friends -> {
            if(isSignedIn) {
                GomantleFriendsScreen(
                    modifier = modifier
                )
            } else {
                RecommendSignIn(
                    modifier = modifier,
                    startSignIn = startSignIn
                )
            }
        }
        ViewType.Rank -> {
            if(isSignedIn) {
                GomantleRankScreen(
                    modifier = modifier,
                    viewModel = viewModel,
                    loadMore = {
                        if(!isLoading && !isAllLoaded) {
                            viewModel.loadMore()
                        }
                    }
                )
            } else {
                RecommendSignIn(
                    modifier = modifier,
                    startSignIn = startSignIn
                )
            }
        }
        ViewType.MyPage -> {
            if(isSignedIn) {
                GomantleMyPageScreen(
                    modifier = modifier
                )
            } else {
                RecommendSignIn(
                    modifier = modifier,
                    startSignIn = startSignIn
                )
            }

        }
    }
}

@Composable
fun RecommendSignIn(
    modifier: Modifier,
    startSignIn: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign in to use",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Sign In",
            modifier = Modifier
                .clickable {
                    startSignIn()
                },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
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