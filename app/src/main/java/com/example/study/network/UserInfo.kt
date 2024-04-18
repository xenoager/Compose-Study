package com.example.study.network

object UserInfo {
    var accessToken: String = ""
    var userId: String = ""
    var userEmail: String = ""

    fun clearData() {
        accessToken = ""
        userId = ""
        userEmail = ""
    }
}