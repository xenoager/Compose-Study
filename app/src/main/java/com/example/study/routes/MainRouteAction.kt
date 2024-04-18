package com.example.study.routes

import androidx.navigation.NavHostController
import com.example.study.R

const val ActivityCloseActionName = "CLOSE_ACTION"
enum class ActivityCloseAction(val actionName: String) {
    POST_ADDED("POST_ADDED"),
    POST_EDITED("POST_EDITED"),
    POST_DELETED("POST_DELETED");

    companion object {
        fun getActionType(name: String): ActivityCloseAction? {
            return when(name) {
                POST_ADDED.actionName -> return POST_ADDED
                POST_EDITED.actionName -> return POST_EDITED
                POST_DELETED.actionName -> return POST_DELETED
                else -> return null
            }
        }
    }
}

sealed class MainRoute(
    open val routeName: String,
    open val title: String,
    open val iconResId: Int? = null
) {
    object Home: MainRoute("HOME", "홈", R.drawable.ic_home)
    object MyPage: MainRoute("MyPage", "마이페이지", R.drawable.ic_profile)
    object AddPost: MainRoute("ADD_POST", "포스트 추가")
    class EditPost(val postId: String): MainRoute("EDIT_POST", "포스트 수정")
}

// 인증 관련 화면 라우트 액션
class MainRouteAction(navHostController: NavHostController) {
    // 특정 라우트 이동
    val navTo: (MainRoute) -> Unit = { mainRoute ->
        navHostController.navigate(mainRoute.routeName) {
            popUpTo(mainRoute.routeName) { inclusive = true }
        }
    }

    // 뒤로 가기 이동
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }
}