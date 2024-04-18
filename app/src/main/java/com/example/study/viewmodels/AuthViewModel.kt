package com.example.study.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study.network.AuthRepository
import com.example.study.network.UserInfo
import com.example.study.network.data.AuthResponse
import com.example.study.network.data.User
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel: ViewModel() {

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    val isLoadingFlow = MutableStateFlow<Boolean>(false)

    var registerCompleteFlow = MutableSharedFlow<Unit>()

    var emailInputFlow = MutableStateFlow<String>("")
    var passwordInputFlow = MutableStateFlow<String>("")
    var passwordConfirmInputFlow = MutableStateFlow<String>("")

    var currentUserEmailFlow = MutableStateFlow<String>("")
    var currentUserIdFlow = MutableStateFlow<String>("")

    fun register() {
        viewModelScope.launch {
            callResister()
        }
    }

    fun login() {
        viewModelScope.launch {
            callLogin()
        }
    }

    private suspend fun callResister() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callResister 회원가입 시작")
                isLoadingFlow.emit(true)
                delay(1500)
                AuthRepository.register(emailInputFlow.value, passwordInputFlow.value)
            }.onSuccess {
                Log.d("kts", "callResister 회원가입 성공")
                if (it.status.value == 200) {
                    registerCompleteFlow.emit(Unit)
                }
                clearInput()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callResister 회원가입 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    private suspend fun callLogin() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callResister 로그인 시작")
                isLoadingFlow.emit(true)
                delay(1500)
                AuthRepository.login(emailInputFlow.value, passwordInputFlow.value)
            }.onSuccess {
                if (it.status.value == 200) {
                    Log.d("kts", "callResister 로그인 성공")
                    isLoggedIn.emit(true)

                    val authResponse = it.body<AuthResponse>()

                    UserInfo.accessToken = authResponse.accessToken ?: ""
                    UserInfo.userId = authResponse.user?.id ?: ""
                    UserInfo.userEmail = authResponse.user?.email ?: ""

                    currentUserEmailFlow.emit(UserInfo.userEmail)
                    currentUserIdFlow.emit(UserInfo.userId)
                }else{
                    Log.d("kts", "callResister 로그인 실패 status : ${it.status.value}")
                }

                clearInput()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callResister 로그인 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    suspend fun clearInput(){
        emailInputFlow.emit("")
        passwordInputFlow.emit("")
        passwordConfirmInputFlow.emit("")
    }

    fun clearUserInfo(){
        UserInfo.clearData()
    }
}