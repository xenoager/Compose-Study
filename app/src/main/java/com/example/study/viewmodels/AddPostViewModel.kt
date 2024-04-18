package com.example.study.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study.network.PostRepository
import com.example.study.network.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPostViewModel: ViewModel() {

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    val isLoadingFlow = MutableStateFlow<Boolean>(false)

    var addPostCompleteFlow = MutableSharedFlow<Unit>()

    var titleInputFlow = MutableStateFlow<String>("")
    var contentInputFlow = MutableStateFlow<String>("")

    var currentUserEmailFlow = MutableStateFlow<String>("")
    var currentUserIdFlow = MutableStateFlow<String>("")



    fun addPost() {
        viewModelScope.launch {
            callAddPost()
        }
    }

    private suspend fun callAddPost() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callAddPost 포스팅 시작")
                isLoadingFlow.emit(true)
                delay(1500)
                PostRepository.addPostItem(titleInputFlow.value, contentInputFlow.value, UserInfo.userId)
            }.onSuccess {
                Log.d("kts", "callAddPost 포스팅 성공")
                if (it.status.value == 201) {
                    addPostCompleteFlow.emit(Unit)
                }
                clearInput()
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callAddPost 포스팅 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    suspend fun clearInput(){
        titleInputFlow.emit("")
        contentInputFlow.emit("")
    }
}