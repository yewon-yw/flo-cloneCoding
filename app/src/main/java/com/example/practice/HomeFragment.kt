package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.practice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>() // Album 데이터 클래스 배열 생성

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeAlbumImgIv1.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }

        // 데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter","방탄소년단 (BTS)",R.drawable.img_album_exp))
            add(Album("Lilac","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Next Level","에스파 (AESPA)",R.drawable.img_album_exp3))
            add(Album("Boy with Luv","방탄소년단(BTS)",R.drawable.img_album_exp4))
            add(Album("BBoom BBoom","모모랜드 (MOMOLAND)",R.drawable.img_album_exp5))
            add(Album("Weekend","태연 (Tae Yeon)",R.drawable.img_album_exp6))
        }
        // adapter 생성 및 연결
        val albumRVAdapter=AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter=albumRVAdapter
        // layoutManager 연결
        binding.homeTodayMusicAlbumRv.layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        // 어댑터 외부의 클릭 이벤트까지 처리해주기 위해 어댑터 외부에있는 homeFragment에서 리스너 객체를 던져줌
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                (context as MainActivity).supportFragmentManager.beginTransaction() // albumFragment로 전환하는 코드
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
            }
        })

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