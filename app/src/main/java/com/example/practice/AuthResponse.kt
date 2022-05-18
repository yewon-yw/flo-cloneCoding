package com.example.practice

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: Int,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: Result?
    )
// 회원가입 API와 로그인 API는 같은 데이터 클래스로 응답값을 받고 있기 때문에
// 회원가입 API에서는 사용하지 않는 result값이 들어가있음
// 따라서 Result?로 null처리 해주기!

data class Result(
    @SerializedName(value = "userIdx") val userIdx: Int,
    @SerializedName(value = "jwt") val jwt: String
)
