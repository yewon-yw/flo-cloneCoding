package com.example.practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // 3개의 프레그먼트를 만들 것이기 때문에 3으로 설정

    override fun createFragment(position: Int): Fragment{
        return when(position){
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}