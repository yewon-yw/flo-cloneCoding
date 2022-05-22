package com.example.practice

import com.example.practice.data.remote.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}