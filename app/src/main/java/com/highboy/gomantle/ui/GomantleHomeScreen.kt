package com.highboy.gomantle.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highboy.gomantle.R
import com.highboy.gomantle.data.ViewType
import com.highboy.gomantle.state.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.highboy.gomantle.ui.state.GomantleViewModel

@Composable
fun GomantleHomeScreen(
    viewModel: GomantleViewModel = viewModel(),
    startSignIn: () -> Unit
) {

    val navigationItemContentList = listOf(
        NavigationItemContent(
            viewType = ViewType.Game,
            icon = R.drawable.stadia_controller_48px,
            text = "Game"
        ), NavigationItemContent(
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
    GomantleScaffold(
        topBar = {
            GomantleTopAppBar()
        },
        bottomBar = {
            GomantleBottomNavigationBar(
                navigationItemContentList = navigationItemContentList)
        },
        startSignIn = {
            startSignIn()
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GomantleScaffold(
    viewModel: GomantleViewModel = viewModel(),
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    startSignIn: () -> Unit
) {
    Scaffold(
        topBar = {
            topBar()
        },
        bottomBar = {
            bottomBar()
        }
    ) {
        GomantleOnlyContentView(
            paddingValues = it,
            currentTab = viewModel.globalStateFlow.uiState.collectAsState().value,
            startSignIn = startSignIn
        )
    }
}

//@Composable
//private fun GomantleAppContent(
//    viewModel: GomantleViewModel = viewModel(),
//    navigationItemContentList: List<NavigationItemContent>,
//    startSignIn: () -> Unit
//) {
//    Column() {
//        GomantleOnlyContentView(
//            modifier = Modifier.weight(13f),
//            currentTab = viewModel.globalStateFlow.uiState.collectAsState().value,
//            startSignIn = startSignIn
//        )
//        GomantleBottomNavigationBar(
//            navigationItemContentList = navigationItemContentList,
//            modifier = Modifier.weight(1f)
//        )
//    }
//}

@Composable
fun GomantleOnlyContentView(
    paddingValues: PaddingValues,
    viewModel: GomantleViewModel = viewModel(),
    currentTab: ViewType,
    startSignIn: () -> Unit
) {
    val isLoading = viewModel.isLoading.collectAsState().value
    val isAllLoaded = viewModel.isAllLoaded.collectAsState().value

    when(currentTab) {
        ViewType.Game -> {
            GomantleGameScreen(
                paddingValues = paddingValues
            )
        }
        ViewType.Friends -> {
            if(viewModel.globalStateFlow.isSignedIn.collectAsState().value) {
                GomantleFriendsScreen(
                    paddingValues = paddingValues
                )
            } else {
                RecommendSignIn(
                    paddingValues = paddingValues,
                    startSignIn = startSignIn
                )
            }
        }
        ViewType.Rank -> {
            if(viewModel.globalStateFlow.isSignedIn.collectAsState().value) {
                GomantleRankScreen(
                    paddingValues = paddingValues,
                    loadMore = {
                        if(!isLoading && !isAllLoaded) {
                            viewModel.loadMore()
                        }
                    }
                )
            } else {
                RecommendSignIn(
                    paddingValues = paddingValues,
                    startSignIn = startSignIn
                )
            }
        }
        ViewType.MyPage -> {
            if(viewModel.globalStateFlow.isSignedIn.collectAsState().value) {
                GomantleMyPageScreen(
                    paddingValues = paddingValues
                )
            } else {
                RecommendSignIn(
                    paddingValues = paddingValues,
                    startSignIn = startSignIn
                )
            }

        }
    }
}

@Composable
fun RecommendSignIn(
    paddingValues: PaddingValues,
    startSignIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
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
    viewModel: GomantleViewModel = viewModel(),
    navigationItemContentList: List<NavigationItemContent>,
) {
    NavigationBar(
        modifier = Modifier
            .height(50.dp),
//        containerColor = Color(255, 200, 155)
    ) {
        navigationItemContentList.forEachIndexed { _, navItem ->
            NavigationBarItem(
                selected = viewModel.globalStateFlow.uiState.collectAsState().value == navItem.viewType,
                onClick = {
                    viewModel.updateCurrentView(navItem.viewType)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GomantleTopAppBar(
    viewModel: GomantleViewModel = viewModel()
) {
    TopAppBar(
        title = {
            when(viewModel.globalStateFlow.uiState.collectAsState().value) {
                ViewType.Game -> TopBarText("Game")
                ViewType.Friends -> TopBarText("Friends")
                ViewType.Rank -> TopBarText("Rank")
                else -> TopBarText("My Page")
            }
        }
    )
}

@Composable
fun TopBarText(
    text: String
) {
    Text(
        text = text,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold
    )
}

private data class NavigationItemContent(
    val viewType: ViewType,
    @DrawableRes
    val icon: Int,
    val text: String
)