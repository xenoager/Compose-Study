package com.example.study.data.moedel

enum class MsgType {
    ME, BOT
}

// 메시지 데이터 클래스
data class Msg (val content: String, val type: MsgType)