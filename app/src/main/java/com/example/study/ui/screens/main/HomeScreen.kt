package com.example.study.ui.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.network.data.Post
import com.example.study.routes.AuthRouteAction
import com.example.study.routes.MainRoute
import com.example.study.routes.MainRouteAction
import com.example.study.ui.component.SimpleDialog
import com.example.study.ui.component.SnsAddPostButton
import com.example.study.ui.component.SnsDialogAction
import com.example.study.ui.theme.Dark
import com.example.study.viewmodels.AuthViewModel
import com.example.study.viewmodels.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    mainRouteAction: MainRouteAction
) {

    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val isLoading by homeViewModel.isLoadingFlow.collectAsState()
    val postListScrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var selectedPostIdforDelete: String? by remember {
        mutableStateOf(null)
    }

    val isDialogShown = !selectedPostIdforDelete.isNullOrBlank()

    val posts = homeViewModel.postsFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        homeViewModel.dataUpdateFlow.collectLatest {
            selectedPostIdforDelete = null
            postListScrollState.animateScrollToItem(posts.value.size)
        }
    })

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { homeViewModel.refreshData() },
        ) {
            Column {
                Surface(color = Dark,
                    contentColor = Color.White) {
                    Text(text = "총 포스팅 : ${posts.value.size}", fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth())
                }

                LazyColumn(
                    state = postListScrollState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp),
                    reverseLayout = true
                ) {
                    items(posts.value) { aPost ->
                        PostItemView(
                            data = aPost,
                            coroutineScope = coroutineScope,
                            homeViewModel = homeViewModel,
                            authViewModel = authViewModel,
                            onDeletePostClicked = {
                                selectedPostIdforDelete = aPost.id.toString()
                            })
                    }
                }
            }
        }

        SnsAddPostButton(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            onClick = {
            coroutineScope.launch {
                homeViewModel.navAction.emit(MainRoute.AddPost)
            }
        })

        if(isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .scale(0.7f)
                    .padding(5.dp)
            )
        }

        if(isDialogShown) {
            SimpleDialog(isLoading = isLoading, onDialogAction = {
                when(it) {
                    SnsDialogAction.CLOSE -> selectedPostIdforDelete = null
                    SnsDialogAction.ACTION -> {
                        Log.d("kts", "아이템 삭제해야함: $selectedPostIdforDelete")
                        selectedPostIdforDelete?.let {postId ->
                            homeViewModel.deletePostItem(postId)
                        }
                    }
                    else -> {}
                }
            })
        }
    }
}

@Composable
fun PostItemView(
    data: Post,
    coroutineScope: CoroutineScope,
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    onDeletePostClicked: () -> Unit) {

    val currentUserId = authViewModel.currentUserIdFlow.collectAsState()

    Surface(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Column {
            Text(text = "userId: ${data.userID}")
            // 첫번째 줄
            Row() {
                Text(text = "${data.id}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )

                if(currentUserId.value == data.userID.toString()) {
                    Row() {
                        TextButton(onClick = {
                            onDeletePostClicked()
                        }) {
                            Text(text = "삭제")
                        }

                        TextButton(onClick = {
                            coroutineScope.launch {
                                homeViewModel.navAction.emit(MainRoute.EditPost(postId = "${data.id}"))
                            }
                        }) {
                            Text(text = "수정")
                        }
                    }
                }
            }
            // 두번째 줄
            Text(
                text = "${data.title} - title",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "${data.content} - content",
                maxLines = 5,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}

