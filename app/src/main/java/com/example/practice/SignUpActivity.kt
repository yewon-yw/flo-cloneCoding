package com.example.practice

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivitySignupBinding

class SignUpActivity:AppCompatActivity() {
    lateinit var binding:ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener{ // 사용자가 가입 완료 버튼 누르면 가입 진행
            signUp()
            finish() // 회원가입 액티비티 종료
        }
    }

    private fun getUser(): User { // 사용자가 입력한 정보로 user 객체 생성후 리턴
        val email: String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()

        return User(email,pwd)
    }

    private fun signUp() { // 회원가입 하는 함수
        if(binding.signUpIdEt.text.toString().isEmpty()||binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signUpPasswordEt.text.toString()!=binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user= userDB.userDao().getUsers() // DB에 잘 들어갔는지 확인하기 위해 Log 출력
        Log.d("SIGNUPACT",user.toString())
    }
}