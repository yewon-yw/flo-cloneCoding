package com.example.practice

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users") // 엔드포인트 작성
    fun signUp(@Body user: User): Call<AuthResponse>
}