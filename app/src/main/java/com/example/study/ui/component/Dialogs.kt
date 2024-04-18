package com.example.study.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class SnsDialogAction {
    CLOSE, ACTION
}

@Composable
fun SimpleDialog(
    isLoading: Boolean,
    onDialogAction: (SnsDialogAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDialogAction(SnsDialogAction.CLOSE)
        },
        title = {
            Text(text = "안내")
        },
        text = {
            Text(text = "정말 해당 포스팅을 삭제하시겠습니까?")
        },
        confirmButton = {
            Button(onClick = {onDialogAction(SnsDialogAction.ACTION)}) {
                if(isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .scale(1f)
                            .size(20.dp)
                    )
                }else{
                    Text(text = "삭제")
                }
            }
        },
        dismissButton = {
            Button(onClick = {
                onDialogAction(SnsDialogAction.CLOSE)
            }) {
                Text(text = "닫기")
            }
        }
    )
}