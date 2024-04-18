package com.example.study.network

import com.example.study.network.data.AuthRequest
import com.example.study.network.data.AuthResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import java.net.URLEncoder

object AuthRepository {

    private const val registerUrl = "https://yoaeohekelkswloohwmq.supabase.co/auth/v1/signup"
    private const val loginUrl = "https://yoaeohekelkswloohwmq.supabase.co/auth/v1/token?grant_type=password"

    private const val TAG: String = "kts"

    suspend fun register(email: String, password: String): HttpResponse {
        return KtorClient.httpClient.post(registerUrl) {
            setBody(AuthRequest(email, password))
        }
    }

    suspend fun login(email: String, password: String): HttpResponse {
        return KtorClient.httpClient.post(loginUrl) {
            url {
//                parameters.append("grant_type", password)
            }
            setBody(AuthRequest(email, password))
        }
    }


}