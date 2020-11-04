package com.example.login.http

/**
Created by chene on @date 20-11-3 下午9:40
 **/
data class RequestBody(
        val url: String,
        val params: Map<String, String>
)