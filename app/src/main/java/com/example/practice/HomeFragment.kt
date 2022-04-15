package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.practice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

        // viewPager 사용을 위해 BannerVPAdapter.kt의 fragmentlist 안에 fragment를 추가해줌
        val bannerAdapter = BannerVPAdapter(this)

        // 여러개의 배너 이미지를 넣기 위해 BannerFragment 클래스의 인자값으로 넣고 싶은 이미지를 넣어줌
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        // viewPager와 Adapter 연결
        binding.homeBannerVp.adapter=bannerAdapter
        // viewPager를 수평(좌우)으로 스크롤 할 수 있도록 설정
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }
}