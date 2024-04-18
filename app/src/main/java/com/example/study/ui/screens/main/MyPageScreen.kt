package com.example.study.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.routes.MainRouteAction
import com.example.study.viewmodels.AuthViewModel
import com.example.study.viewmodels.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun MyPageScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    mainRouteAction: MainRouteAction
) {

    val isRefreshing by homeViewModel.isRefreshing.collectAsState()

    val postListScrollState = rememberLazyListState()

    val userId = authViewModel.currentUserIdFlow.collectAsState()
    val userEmail = authViewModel.currentUserEmailFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { homeViewModel.refreshData() },
        ) {
           Column(
               Modifier
                   .fillMaxSize()
                   .padding(20.dp)
           ) {
               Text(text = "마이페이지", fontSize = 30.sp)

               Text(text = "이메일 : ")
               Text(text = userEmail.value)

               Spacer(modifier = Modifier.height(30.dp))

               Text(text = "아이디 : ")
               Text(text = userId.value)

               TextButton(onClick = {
                   coroutineScope.launch {
                       authViewModel.isLoggedIn.emit(false)
                       authViewModel.clearUserInfo()
                   }
               }) {
                   Text(text = "로그아웃")
               }
           }
        }
    }
}