package com.example.study.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.study.routes.AuthRoute
import com.example.study.routes.AuthRouteAction
import com.example.study.ui.component.BaseButton
import com.example.study.ui.component.SnsButtonType

@Composable
fun WelcomeScreen(routeAction: AuthRouteAction) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        lottieAnimationView()

        Text(text = "AiDevU SNS", fontSize = 23.sp, fontWeight = FontWeight.Bold)
 
        Spacer(modifier = Modifier.weight(1f))

        BaseButton(title = "로그인", onClick = {
            Log.d("kts", "로그인 버튼 클릭")
            keyboardController?.hide()
            routeAction.navTo(AuthRoute.LOGIN)
        })

        Spacer(modifier = Modifier.height(15.dp))

       BaseButton(type = SnsButtonType.OUTLINE, title = "회원가입", onClick = {
           Log.d("kts", "회원가입 버튼 클릭")
           keyboardController?.hide()
           routeAction.navTo(AuthRoute.REGISTER)
       })

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun lottieAnimationView() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("social_animation.json")
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.height(400.dp)
    )
}