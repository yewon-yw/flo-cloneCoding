package com.example.practice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://edu-api-test.softsquared.com"

// 여러화면에서 공통적으로 사용되는 함수이기 때문에 따로 class 파일을 만들어서 선언해줌
fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build() // retrofit 객체 생성

    return retrofit
}