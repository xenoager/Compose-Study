package com.example.study.ui.screens.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.MainActivity
import com.example.study.R
import com.example.study.routes.ActivityCloseAction
import com.example.study.routes.ActivityCloseActionName
import com.example.study.routes.AuthRoute
import com.example.study.routes.AuthRouteAction
import com.example.study.ui.component.BaseButton
import com.example.study.ui.component.SnsBackButton
import com.example.study.ui.component.SnsPasswordTextField
import com.example.study.ui.component.SnsTextField
import com.example.study.ui.theme.Border
import com.example.study.ui.theme.Gray
import com.example.study.ui.theme.LightGray
import com.example.study.viewmodels.AddPostViewModel
import com.example.study.viewmodels.AuthViewModel
import com.example.study.viewmodels.EditPostViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditPostScreen(
    editPostViewModel: EditPostViewModel
) {

    val titleInput = editPostViewModel.titleInputFlow.collectAsState()
    val contentInput = editPostViewModel.contentInputFlow.collectAsState()

    val isAddPostBtnActive = titleInput.value.isNotEmpty()

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isLoading = editPostViewModel.isLoadingFlow.collectAsState()
    val isEditPostLoading = editPostViewModel.isEditPostLoadingFlow.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val activity = LocalContext.current as? Activity
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit, block = {
        editPostViewModel.editPostCompleteFlow.collectLatest {
            snackbarHostState
                .showSnackbar("포스트가 수정 되었습니다.", actionLabel = "홈으로", SnackbarDuration.Short)
                .let {
                    when(it) {
                        SnackbarResult.Dismissed -> {
                            Log.d("kts", "스낵바 닫힘")
                        }
                        SnackbarResult.ActionPerformed -> {
                            val intent = Intent(context, MainActivity::class.java).apply {
                                putExtra(ActivityCloseActionName, ActivityCloseAction.POST_EDITED.actionName)
                            }
                            activity?.setResult(Activity.RESULT_OK, intent)
                            activity?.finish()
                        }
                        else -> {}
                    }
                }
        }
    })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState, enabled = true)
        ) {

            Text(
                text = "포스트 수정 화면",
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 30.dp))

            SnsTextField(label = "제목", keyboardType = KeyboardType.Email, singleLine = true, value = titleInput.value, onValueChange = {
                coroutineScope.launch {
                    editPostViewModel.titleInputFlow.emit(it)
                }
            })

            Spacer(modifier = Modifier.height(15.dp))

            SnsTextField(
                modifier = Modifier.height(300.dp),
                label = "내용", value = contentInput.value, singleLine = false, onValueChange = {
                    coroutineScope.launch {
                        editPostViewModel.contentInputFlow.emit(it)
                    }
                })

            Spacer(modifier = Modifier.height(30.dp))

            BaseButton(
                title = "포스트 수정하기",
                enabled = isAddPostBtnActive,
                isLoading = isEditPostLoading.value,
                onClick = {
                    Log.d("kts", "포스트 수정하기 버튼 클릭")
                    if(!isEditPostLoading.value) {
                        keyboardController?.hide()
                        editPostViewModel.editPost()
                    }
                })

            Spacer(modifier = Modifier.weight(1f))

            SnackbarHost(hostState = snackbarHostState)
        }

        if(isLoading.value) {
            Surface(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f)
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier.size(40.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}