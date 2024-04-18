package com.example.study.data.moedel

import kotlinx.serialization.*

@Serializable
data class MessageResponse (
    val status: Long,
    val statusMessage: String,
    val request: Request,
    val atext: String,
    val lang: String
)

@Serializable
data class Request (
    val utext: String,
    val lang: String
)

fun MessageResponse.toMsg(): Msg {
    return Msg(this.atext, type = MsgType.BOT)
}
