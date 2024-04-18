package com.example.study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.study.ui.screens.main.AddPostScreen
import com.example.study.ui.theme.StudyTheme
import com.example.study.viewmodels.AddPostViewModel

class AddPostActivity: ComponentActivity() {

    private val addPostViewModel: AddPostViewModel by viewModels()
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddPostActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            StudyTheme {
                AddPostScreen(addPostViewModel = addPostViewModel)
            }
        }
    }
}