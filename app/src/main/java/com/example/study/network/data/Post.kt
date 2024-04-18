package com.example.study.network.data

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Post (
    val id: Long,
    val title: String,
    val content: String,

    @SerialName("user_id")
    val userID: String? = null,

    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class PostRequest (
    val title: String,
    val content: String?,

    @SerialName("user_id")
    val userID: String? = null
)

@Serializable
data class PostEditRequest (
    val title: String,
    val content: String?,
)