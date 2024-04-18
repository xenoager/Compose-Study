package com.example.study.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.R
import com.example.study.routes.AuthRoute
import com.example.study.routes.AuthRouteAction
import com.example.study.ui.component.BaseButton
import com.example.study.ui.component.SnsBackButton
import com.example.study.ui.component.SnsPasswordTextField
import com.example.study.ui.component.SnsTextField
import com.example.study.ui.theme.Border
import com.example.study.ui.theme.Gray
import com.example.study.ui.theme.LightGray
import com.example.study.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    routeAction: AuthRouteAction,
    authViewModel: AuthViewModel) {

    val emailInput = authViewModel.emailInputFlow.collectAsState()
    val passwordInput = authViewModel.passwordInputFlow.collectAsState()
    val isLoading = authViewModel.isLoadingFlow.collectAsState()

    val isLoginBtnActive = emailInput.value.isNotEmpty() && passwordInput.value.isNotEmpty()

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

        SnsBackButton(
            modifier = Modifier.padding(vertical = 20.dp),
            onClick = {
                Log.d("kts", "뒤로 가기 버튼 클릭")
                keyboardController?.hide()
                routeAction.goBack()
        }
        )

        Text(
            text = "로그인 화면",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 30.dp))

        SnsTextField(label = "이메일", keyboardType = KeyboardType.Email, singleLine = true, value = emailInput.value, onValueChange = {
            coroutineScope.launch {
                authViewModel.emailInputFlow.emit(it)
            }
        })

        Spacer(modifier = Modifier.height(30.dp))

        SnsPasswordTextField(label = "비밀번호", value = passwordInput.value, onValueChange = {
            coroutineScope.launch {
                authViewModel.passwordInputFlow.emit(it)
            }
        })

        Spacer(modifier = Modifier.height(30.dp))
        BaseButton(
            title = "로그인",
            enabled = isLoginBtnActive,
            isLoading = isLoading.value,
            onClick = {
            Log.d("kts", "로그인 버튼 클릭")
                keyboardController?.hide()
                coroutineScope.launch {
                    authViewModel.login()
                }
        })

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "계정이 없으신가요?")
            TextButton(onClick = {
                coroutineScope.launch {
                    authViewModel.clearInput()
                }
                Log.d("kts", "회원가입 하러가기 버튼 클릭")
                keyboardController?.hide()
                routeAction.navTo(AuthRoute.REGISTER)
            }) {
                Text(text = "회원가입 하러가기")
            }
        }
//        TextButton(onClick = {
//            coroutineScope.launch {
//                authViewModel.isLoggedIn.emit(true)
//            }
//        }) {
//            Text(text = "로그인 완료")
//        }
    }
}