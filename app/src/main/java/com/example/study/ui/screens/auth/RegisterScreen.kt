package com.example.study.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.routes.AuthRoute
import com.example.study.routes.AuthRouteAction
import com.example.study.ui.component.BaseButton
import com.example.study.ui.component.SnsBackButton
import com.example.study.ui.component.SnsPasswordTextField
import com.example.study.ui.component.SnsTextField
import com.example.study.viewmodels.AuthViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    routeAction: AuthRouteAction) {

    val emailInput = authViewModel.emailInputFlow.collectAsState()
    val passwordInput = authViewModel.passwordInputFlow.collectAsState()
    val passwordConfirmInput = authViewModel.passwordConfirmInputFlow.collectAsState()
    val isLoading = authViewModel.isLoadingFlow.collectAsState()

    val isPasswordsNotEmpty =
                passwordInput.value.isNotEmpty() &&
                passwordConfirmInput.value.isNotEmpty()

    val isPasswordsMatch = passwordInput.value == passwordConfirmInput.value

    val isRegisterBtnActive = emailInput.value.isNotEmpty() && isPasswordsNotEmpty && isPasswordsMatch

    val scrollState = rememberScrollState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit, block = {
        // 회원가입 성공시
        authViewModel.registerCompleteFlow.collectLatest {
            snackbarHostState
                .showSnackbar(
                    "회원가입 완료: 로그인해주세요!",
                    actionLabel = "확인", SnackbarDuration.Short)
                .let {
                    when(it) {
                        SnackbarResult.Dismissed -> {
                            Log.d("kts","스낵바 닫기 버튼 클릭")
                        }
                        SnackbarResult.ActionPerformed -> {
                            routeAction.navTo(AuthRoute.LOGIN)
                        }
                        else -> {}
                    }
                }
        }
    })

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState, enabled = true),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        SnsBackButton(
            modifier = Modifier.padding(top = 20.dp),
            onClick = {
                Log.d("kts", "뒤로 가기 버튼 클릭")
                keyboardController?.hide()
                routeAction.goBack()
            })

        Text(
            text = "회원가입 화면",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 10.dp))

        SnsTextField(label = "이메일", keyboardType = KeyboardType.Email, singleLine = true, value = emailInput.value, onValueChange = {
            coroutineScope.launch {
                authViewModel.emailInputFlow.emit(it)
            }
        })

        SnsPasswordTextField(
            label = "비밀번호",
            value = passwordInput.value,
            onValueChange = {
                coroutineScope.launch {
                    authViewModel.passwordInputFlow.emit(it)
                }
            })

        SnsPasswordTextField(
            label = "비밀번호 확인",
            value = passwordConfirmInput.value,
            onValueChange = {
                coroutineScope.launch {
                    authViewModel.passwordConfirmInputFlow.emit(it)
                }
            })

        Spacer(modifier = Modifier.height(15.dp))

        BaseButton(
            title = "회원가입",
            enabled = isRegisterBtnActive,
            isLoading = isLoading.value,
            onClick = {
                Log.d("kts", "회원가입 버튼 클릭")
                if(!isLoading.value) {
                    keyboardController?.hide()
                    authViewModel.register()
                }
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "이미 계정이 있으신가요?")
            TextButton(onClick = {
                Log.d("kts", "로그인 하러가기 버튼 클릭")
                coroutineScope.launch {
                     authViewModel.clearInput()
                }
                routeAction.navTo(AuthRoute.LOGIN)
            }) {
                Text(text = "로그인 하러가기")
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}