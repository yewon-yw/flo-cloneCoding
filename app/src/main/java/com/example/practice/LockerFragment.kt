package com.example.practice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private val information= arrayListOf("저장한 곡","음악파일","저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter=LockerVPAdapter(this)
        binding.lockerContentVp.adapter=lockerAdapter
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
                tab,position ->
            tab.text=information[position]
        }.attach()

//        binding.lockerLoginTv.setOnClickListener {
//            startActivity(Intent(activity,LoginActivity::class.java))
//        }

        return binding.root
    }

    override fun onStart() { // 사용자에게 보여지기 직전에 호출
        super.onStart()
        initViews()
    }

    private fun getJwt(): Int {
        // LoginActivity에서 auth라는 이름으로 저장했던 sharedPreference를 가져옴(jwt 받아오기 위해)
        // activity? 작성해주는 이유는 프래그먼트에서 작성할때 쓰는 방법이라고 이해하기
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        // 디폴트값을 0으로 설정해두면 spf에서 받아온 jwt값이 없는 경우엔 0 리턴
        return spf!!.getInt("jwt",0)
    }

    private fun initViews(){
        // spf로 받아온 jwt가 존재한다면 현재 로그인 된 경우이기 때문에 보관함의 로그인 텍스트를 로그아웃으로 변경해줌
        // 그 반대라면 로그인으로 텍스트 설정
        val jwt: Int = getJwt()
        if(jwt == 0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity,LoginActivity::class.java))
            }
        } else{
           binding.lockerLoginTv.text = "로그아웃"
           binding.lockerLoginTv.setOnClickListener {
               // 로그아웃 진행
               logout()
               startActivity(Intent(activity,MainActivity::class.java))
           }
        }
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        // jwt 값을 없애줌
        editor.remove("jwt")
        editor.apply()
    }

}