package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment: Fragment() {
    lateinit var binding:FragmentAlbumBinding

    private val information= arrayListOf("수록곡", "상세정보", "영상")

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
}