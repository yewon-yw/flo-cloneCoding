package com.example.practice

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity:AppCompatActivity() {
    lateinit var binding:ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener{ // 사용자가 가입 완료 버튼 누르면 가입 진행
            signUp()
            //finish() // 회원가입 액티비티 종료
        }
    }

    private fun getUser(): User { // 사용자가 입력한 정보로 user 객체 생성후 리턴
        val email: String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        return User(email,pwd,name)
    }

//    private fun signUp() { // 회원가입 하는 함수 RoomDB 사용
//        if(binding.signUpIdEt.text.toString().isEmpty()||binding.signUpDirectInputEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
//            return
//        }
//        if(binding.signUpPasswordEt.text.toString()!=binding.signUpPasswordCheckEt.text.toString()){
//            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val user= userDB.userDao().getUsers() // DB에 잘 들어갔는지 확인하기 위해 Log 출력
//        Log.d("SIGNUPACT",user.toString())
//    }

    private fun signUp(){
        if(binding.signUpIdEt.text.toString().isEmpty()||binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpNameEt.text.toString().isEmpty()){
            Toast.makeText(this,"이름 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString()!=binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(getUser()).enqueue(object: Callback<AuthResponse>{
            // object에 추가된 메소드를 정의해줘야함
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS",response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code){
                    1000 -> finish() // code가 1000번이면 성공한 경우
                    2016, 2018 -> {
                        binding.signUpEmailErrorTv.visibility=View.VISIBLE
                        binding.signUpEmailErrorTv.text = resp.message
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE",t.message.toString())
            }
        })
        Log.d("SIGNUP","Hello") // 위의 작업이 비동기적이기 때문에 함수 실행이 잘 되었는지 확인하기 위해 log
    }
}