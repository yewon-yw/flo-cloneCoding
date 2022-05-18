package com.example.practice

// Activity와 AuthService를 연결시켜주기 위한 인터페이스
interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(resp: AuthResponse)
}