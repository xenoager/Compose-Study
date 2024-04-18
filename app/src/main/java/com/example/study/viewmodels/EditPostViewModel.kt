package com.example.study.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study.network.PostRepository
import com.example.study.network.UserInfo
import com.example.study.network.data.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPostViewModel(private val currentPostId: String): ViewModel() {
    companion object {
        const val TAG = "kts"
    }

    val isLoggedIn = MutableStateFlow<Boolean>(false)

    val isLoadingFlow = MutableStateFlow<Boolean>(false)
    val isEditPostLoadingFlow = MutableStateFlow<Boolean>(false)

    var editPostCompleteFlow = MutableSharedFlow<Unit>()

    var titleInputFlow = MutableStateFlow<String>("")
    var contentInputFlow = MutableStateFlow<String>("")

    var currentUserEmailFlow = MutableStateFlow<String>("")
    var currentUserIdFlow = MutableStateFlow<String>("")


    init {
        Log.d(TAG, "EditPostViewModel init currentPostId : $currentPostId")
        fetchCurrentPostItem()
    }

    private fun fetchCurrentPostItem() {
        viewModelScope.launch {
            callFetchPostItem()
        }
    }

    fun editPost() {
        viewModelScope.launch {
            callEditPostItem()
        }
    }

    private suspend fun callFetchPostItem() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callFetchPostItem 선택한 포스팅 가져오기 시작")
                isLoadingFlow.emit(true)
                delay(1500)
                PostRepository.fetchPostItem(currentPostId)
            }.onSuccess {
                Log.d("kts", "callFetchPostItem 선택한 포스팅 가져오기 성공")
                titleInputFlow.emit(it.title ?: "")
                contentInputFlow.emit(it.content ?: "")
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callFetchPostItem 선택한 포스팅 가져오기 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    private suspend fun callEditPostItem() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callEditPostItem 선택한 포스팅 수정하기 시작")
                isEditPostLoadingFlow.emit(true)
                delay(1500)
                PostRepository.editPostItem(
                    currentPostId,
                    titleInputFlow.value,
                    contentInputFlow.value
                )
            }.onSuccess {
                if(it.status.value == 200) {
                    editPostCompleteFlow.emit(Unit)
                }
                isEditPostLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callEditPostItem 선택한 포스팅 수정하기 실패 : ${it.localizedMessage}")
                isEditPostLoadingFlow.emit(false)
            }
        }
    }

    suspend fun clearInput(){
        titleInputFlow.emit("")
        contentInputFlow.emit("")
    }
}