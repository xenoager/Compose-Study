package com.example.study.routes

import androidx.navigation.NavHostController

// 인증 화면 라우트
enum class AuthRoute(val routeName: String) {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    WELCOME("WELCOME")
}

// 인증 관련 화면 라우트 액션
class AuthRouteAction(navHostController: NavHostController) {
    // 특정 라우트 이동
    val navTo: (AuthRoute) -> Unit = { authRoute ->
        navHostController.navigate(authRoute.routeName) {
            popUpTo(authRoute.routeName) { inclusive = true }
        }
    }

    // 뒤로 가기 이동
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }
}