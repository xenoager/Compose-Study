package com.example.study.network

import com.example.study.BuildConfig
import com.example.study.network.data.Post
import com.example.study.network.data.PostEditRequest
import com.example.study.network.data.PostRequest
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

object PostRepository {
    private const val postUrl = "https://yoaeohekelkswloohwmq.supabase.co/rest/v1/posts"


    private const val TAG: String = "kts"

    //모든 포스트 가져오기
    suspend fun fetchAllPosts(): List<Post> {
        return KtorClient.httpClient.get(postUrl) {
            url {
                parameters.append("select", "*")
            }
            headers {
                headers.append("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }.body<List<Post>>()
    }

    //단일 포스트 가져오기
    suspend fun fetchPostItem(postId: String): Post {
        return KtorClient.httpClient.get(postUrl) {
            url {
                parameters.append("select", "*")
                parameters.append("id", "eq.$postId")
            }
            headers {
                headers.append("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }.body<List<Post>>()[0]
    }

    //단일 포스트 삭제
    suspend fun deletePostItem(postId: String): HttpResponse {
        return KtorClient.httpClient.delete(postUrl) {
            url {
                parameters.append("id", "eq.$postId")
            }
            headers {
                headers.append("Authorization", "Bearer ${UserInfo.accessToken}")
//                headers.append("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }
    }

    //단일 포스트 추가하기
    suspend fun addPostItem(title: String, content: String? = null, userId: String?= null): HttpResponse {
        return KtorClient.httpClient.post(postUrl) {
            headers {
                headers.append("Authorization", "Bearer ${UserInfo.accessToken}")
//                headers.append("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
            }
            setBody(PostRequest(title, content, userId))
        }
    }

    //단일 포스트 수정하기
    suspend fun editPostItem(postId: String, title: String, content: String? = null): HttpResponse {
        return KtorClient.httpClient.patch(postUrl) {
            url {
                parameters.append("id", "eq.$postId")
            }
            headers {
                headers.append("Authorization", "Bearer ${UserInfo.accessToken}")
//                headers.append("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
                headers.append("Prefer", "return=representation")
            }
            setBody(PostEditRequest(title, content))
        }
    }
}