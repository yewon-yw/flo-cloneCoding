package com.example.practice

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}