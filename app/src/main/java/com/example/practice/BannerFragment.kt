package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentBannerBinding

class BannerFragment(val imgRes:Int) : Fragment() {

    // 하나의 프래그먼트에 하나의 이미지를 설정하기 위해 fragment_banner.xml파일에 이미지 뷰를 생성해 이미지를 하나 넣어줌
    // 만든 xml파일을 현재 프래그먼트와 연결해주기 위해 바인딩 선언
    lateinit var binding : FragmentBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBannerBinding.inflate(inflater,container,false)

        // 이 클래스의 인자값으로 받은 imgRes를 바인딩으로 연결된 프래그먼트 내의 이미지뷰의 src로 설정
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }
}