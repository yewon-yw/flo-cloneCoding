package com.example.practice.data.remote

import android.util.Log
import com.example.practice.LoginView
import com.example.practice.SignUpView
import com.example.practice.data.entities.User
import com.example.practice.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Service에서 SignUpView를 받을 변수를 클래스로 정의
class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    // 회원가입 Api를 호출하고 관리하는 역할을 하는 메소드
    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            // object에 추가된 메소드를 정의해줘야함
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS",response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE",t.message.toString())
            }
        })
        Log.d("SIGNUP","Hello") // 위의 작업이 비동기적이기 때문에 함수 실행이 잘 되었는지 확인하기 위해 log
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object: Callback<AuthResponse> {
            // object에 추가된 메소드를 정의해줘야함
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS",response.toString())
                val resp: AuthResponse = response.body()!!
                when(val code = resp.code){
                    1000 -> loginView.onLoginSuccess(code, resp.result!!)
                    else -> loginView.onLoginFailure()
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE",t.message.toString())
            }
        })
        Log.d("SIGNUP","Hello") // 위의 작업이 비동기적이기 때문에 함수 실행이 잘 되었는지 확인하기 위해 log
    }
}