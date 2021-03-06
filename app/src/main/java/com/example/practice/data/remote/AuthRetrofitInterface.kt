package com.example.practice.data.remote

import com.example.practice.data.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users") // 엔드포인트 작성
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login") // 엔드포인트 작성
    fun login(@Body user: User): Call<AuthResponse>
}