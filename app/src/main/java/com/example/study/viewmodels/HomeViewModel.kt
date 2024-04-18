package com.example.study.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study.network.AuthRepository
import com.example.study.network.PostRepository
import com.example.study.network.data.Post
import com.example.study.routes.MainRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {

    companion object {
        const val TAG = "kts"
    }

    val isRefreshing = MutableStateFlow<Boolean>(false)
    val navAction = MutableSharedFlow<MainRoute>()
    val isLoadingFlow = MutableStateFlow<Boolean>(false)
    val postsFlow = MutableStateFlow<List<Post>>(emptyList())
    val dataUpdateFlow = MutableSharedFlow<Unit>()

    init {
        fetchPost()
    }

    fun refreshData() {
        Log.d(TAG, "refresh() call")
        viewModelScope.launch {
            isLoadingFlow.emit(true)
            delay(1000)
            callFetchPosts()
        }
    }

    fun fetchPost() {
        viewModelScope.launch {
            callFetchPosts()
        }
    }

    fun deletePostItem(postId: String) {
        viewModelScope.launch {
            callDeletePostItem(postId)
        }
    }

    private suspend fun callFetchPosts() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callResister 회원가입 시작")
                isLoadingFlow.emit(true)
                delay(1500)
                PostRepository.fetchAllPosts()
            }.onSuccess {
                Log.d("kts", "callResister 회원가입 성공")
                postsFlow.emit(it)
                dataUpdateFlow.emit(Unit)
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callFetchPosts 포스트 가져오기 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }

    private suspend fun callDeletePostItem(postId: String) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d("kts", "callDeletePostItem 삭제 시작")
                isLoadingFlow.emit(true)
                PostRepository.deletePostItem(postId)
            }.onSuccess {
                if(it.status.value == 204) {
                    Log.d("kts", "callDeletePostItem 삭제 성공")
                    refreshData()
                }
                isLoadingFlow.emit(false)
            }.onFailure {
                Log.d("kts", "callDeletePostItem 포스트 삭제 실패 : ${it.localizedMessage}")
                isLoadingFlow.emit(false)
            }
        }
    }
}