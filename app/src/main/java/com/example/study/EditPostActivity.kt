package com.example.study

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.study.ui.screens.main.AddPostScreen
import com.example.study.ui.screens.main.EditPostScreen
import com.example.study.ui.theme.StudyTheme
import com.example.study.viewmodels.EditPostViewModel

class EditPostActivity: ComponentActivity() {

    lateinit var editPostViewModel: EditPostViewModel

    companion object {
        private const val POST_ID = "post_id"
        fun newIntent(context: Context, postId: String): Intent {
            return Intent(context, EditPostActivity::class.java).apply {
                putExtra(POST_ID, postId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val postId: String = intent.getStringExtra(POST_ID) ?: ""

        editPostViewModel = EditPostViewModel(postId)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            StudyTheme {
                EditPostScreen(editPostViewModel = editPostViewModel)
            }
        }
    }
}