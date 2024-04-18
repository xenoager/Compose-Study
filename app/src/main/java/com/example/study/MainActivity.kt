package com.example.study

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.study.routes.ActivityCloseAction
import com.example.study.routes.ActivityCloseActionName
import com.example.study.routes.AuthRoute
import com.example.study.routes.AuthRouteAction
import com.example.study.routes.MainRoute
import com.example.study.routes.MainRouteAction
import com.example.study.ui.screens.auth.LoginScreen
import com.example.study.ui.screens.auth.RegisterScreen
import com.example.study.ui.screens.auth.WelcomeScreen
import com.example.study.ui.screens.main.HomeScreen
import com.example.study.ui.screens.main.MyPageScreen
import com.example.study.ui.theme.*
import com.example.study.viewmodels.AuthViewModel
import com.example.study.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun main() {

}

// API 키 : _.dO0Z0OMGtjqKsRfHgh1u19PGJKFEbXbVTbjGEg

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    companion object {
        const val TAG: String = "kts"
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            val getActionString = result.data?.getStringExtra(ActivityCloseActionName)

            val closeAction = ActivityCloseAction.getActionType(getActionString?: "")
            closeAction.let {
                homeViewModel.refreshData()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        lifecycleScope.launch {
            homeViewModel.navAction.collectLatest {
                when(it) {
                    is MainRoute.AddPost -> {
                        activityResultLauncher.launch(AddPostActivity.newIntent(this@MainActivity))
                    }

                    is MainRoute.EditPost -> {
                        activityResultLauncher.launch(EditPostActivity.newIntent(this@MainActivity, it.postId))
                    }

                    else -> {

                    }
                }
            }
        }

        setContent {
            StudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppScreen(authViewModel, homeViewModel)
                }
            }
        }
    }
}

@Composable
fun AppScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel
) {

    val isLoggedIn = authViewModel.isLoggedIn.collectAsState()

    val authNavHostController = rememberNavController()
    val authRouteAction = remember(authNavHostController) {
        AuthRouteAction(authNavHostController)
    }

    val mainNavHostController = rememberNavController()
    val mainRouteAction = remember(mainNavHostController) {
        MainRouteAction(mainNavHostController)
    }

    val mainBackStackEntry = mainNavHostController.currentBackStackEntryAsState()

    if (!isLoggedIn.value) {
        AuthNavHost(
            authNavHostController = authNavHostController,
            authViewModel = authViewModel,
            routeAction = authRouteAction
        )
    } else {
        Scaffold(
            bottomBar = {
                SnsBottomNav(mainRouteAction = mainRouteAction, mainBackStack = mainBackStackEntry.value)
            }
        ) {
            Column(
                Modifier.padding(bottom = it.calculateBottomPadding())
            ) {
                MainNavHost(
                    mainNavHostController = mainNavHostController,
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    routeAction = mainRouteAction
                )
            }
        }
    }
}

@Composable
fun MainNavHost(
    mainNavHostController: NavHostController,
    startRoute: MainRoute = MainRoute.Home,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    routeAction: MainRouteAction
) {
    NavHost(navController = mainNavHostController, startDestination = startRoute.routeName) {
        composable(MainRoute.Home.routeName) {
            HomeScreen(homeViewModel, authViewModel, routeAction)
            Log.d("kts", "HomeScreen")
        }
        composable(MainRoute.MyPage.routeName) {
            MyPageScreen(homeViewModel, authViewModel = authViewModel, routeAction)
            Log.d("kts", "MyPageScreen")
        }
    }
}

@Composable
fun AuthNavHost(authNavHostController: NavHostController,
                startRoute: AuthRoute = AuthRoute.WELCOME,
                authViewModel: AuthViewModel,
                routeAction: AuthRouteAction
) {
    NavHost(navController = authNavHostController, startDestination = startRoute.routeName) {
        composable(AuthRoute.WELCOME.routeName) {
            WelcomeScreen(routeAction)
        }
        composable(AuthRoute.LOGIN.routeName) {
            LoginScreen(routeAction, authViewModel = authViewModel)
        }
        composable(AuthRoute.REGISTER.routeName) {
            RegisterScreen(authViewModel, routeAction)
        }
    }
}

@Composable
fun SnsBottomNav(
    mainRouteAction: MainRouteAction,
    mainBackStack: NavBackStackEntry?
) {
    val bottomRoutes = listOf<MainRoute>(MainRoute.Home, MainRoute.MyPage)
    BottomNavigation(
        backgroundColor = LightGray,
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomRoutes.forEach {
            BottomNavigationItem(
                label = { Text(text = it.title)},
                selectedContentColor = Dark,
                unselectedContentColor = Gray,
                selected = (mainBackStack?.destination?.route) == it.routeName,
                onClick = {
                          mainRouteAction.navTo(it)
                },
                icon = { it.iconResId?.let {iconId ->
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = it.title)
                } })
        }
    }
}