package com.example.study.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val email: String,
    val password: String
)