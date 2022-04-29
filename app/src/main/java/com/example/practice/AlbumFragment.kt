package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment: Fragment() {
    lateinit var binding:FragmentAlbumBinding

    private val information= arrayListOf("수록곡", "상세정보", "영상")
    private val gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAlbumBinding.inflate(inflater,container,false)
        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumJson = arguments?.getString("album") // arguments에서 데이터를 꺼냄
        val album=gson.fromJson(albumJson,Album::class.java) // Gson을 사용해서 Json을 Album객체로 바꿔줌
        setInit(album) // 받아온 데이터로 위젯 변경하는 함수

//        binding.songLalacLayout.setOnClickListener{
//            Toast.makeText(activity,"Lilac", Toast.LENGTH_SHORT).show()
//        }
        val albumAdapter = AlbumVPAdapter(this) // 초기화
        binding.albumContentVp.adapter=albumAdapter
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
                tab, position ->
            tab.text =information[position]
        }.attach()
        return binding.root
    }

    private fun setInit(album:Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text=album.title.toString()
        binding.albumSingerNameTv.text=album.singer.toString()
    }
}