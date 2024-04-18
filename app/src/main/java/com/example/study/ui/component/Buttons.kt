package com.example.study.ui.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.study.R
import com.example.study.ui.theme.Border
import com.example.study.ui.theme.Dark
import com.example.study.ui.theme.Gray

// 버튼 타입
enum class SnsButtonType {
    FILL, OUTLINE
}

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    type: SnsButtonType = SnsButtonType.FILL,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    when(type) {
        SnsButtonType.FILL -> {
            SnsFilledButton(modifier, title, enabled, isLoading, onClick)
        }
        SnsButtonType.OUTLINE -> {
            SnsOutlinedButton(modifier, title, enabled, isLoading, onClick)
        }
    }
}

@Composable
fun SnsFilledButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            backgroundColor = Dark,
            disabledContentColor = Color.White,
            disabledBackgroundColor = Gray
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .scale(1f)
                    .size(20.dp)
            )
        }else{
            Text(text = title,
                fontSize = 15.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun SnsOutlinedButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Dark,
            backgroundColor = Color.White,
            disabledContentColor = Dark,
            disabledBackgroundColor = Color.White
        ),
        enabled = enabled,
        border = BorderStroke(1.dp, Dark),
        onClick = onClick
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                color = Dark,
                modifier = Modifier
                    .scale(1f)
                    .size(20.dp)
            )
        }else {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Dark,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun SnsBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .size(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Dark,
            backgroundColor = Color.White,
            disabledContentColor = Dark,
            disabledBackgroundColor = Color.White
        ),
        border = BorderStroke(1.dp, Border),
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "뒤로가기 버튼")
    }
}

@Composable
fun SnsAddPostButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .size(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            backgroundColor = Dark,
        ),
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = R.drawable.ic_pen),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "포스트 추가하기 버튼")
    }
}